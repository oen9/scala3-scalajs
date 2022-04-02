package oen9.jvm.endpoints

import cats.effect.Async
import cats.implicits.*
import oen9.jvm.Model.SrvInfo
import org.http4s.HttpRoutes
import sttp.tapir._
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.http4s.Http4sServerInterpreter

import java.net.InetAddress

object AppEndpoints:
  val check = endpoint
    .summary("is server alive?")
    .get
    .in("check")
    .out(jsonBody[String])

  val hostname = endpoint
    .summary("get hostname")
    .get
    .in("hostname")
    .out(jsonBody[SrvInfo])

  def checkRoute[F[_]: Async]() =
    Http4sServerInterpreter[F]().toRoutes(check.serverLogic(_ => Async[F].pure("ok".asRight)))
  def hostnameRoute[F[_]: Async]() = Http4sServerInterpreter[F]().toRoutes(
    hostname.serverLogic(_ =>
      for {
        hostname <- Async[F].delay(InetAddress.getLocalHost.getHostName)
      } yield SrvInfo(hostname = hostname).asRight
    )
  )

  def routes[F[_]: Async]() = checkRoute() <+>
    hostnameRoute()
  def endpoints = List(check, hostname)

  // this doesn't work, no websock upgrade (Unknown connection header: 'Upgrade'. Closing connection upon completion.)
  object tapirWebsockOLD:
    import sttp.capabilities.fs2.Fs2Streams
    import fs2.Stream
    import scala.concurrent.duration._
    def randomStream[F[_]]() = endpoint
      .in("tapirwebsock")
      .out(webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.Json](Fs2Streams[F]))
    // def randomStreamRoute[F[_]: Async]() =
    //  Http4sServerInterpreter[F]().toRoutes(randomStream[F]())(_ =>
    //    Async[F].delay(((_: Stream[F, String]) => Stream.awakeEvery(2.second).map(x => s"tapir $x")).asRight)
    //  )
  object tapirRawWebsockOLD:
    import sttp.capabilities.fs2.Fs2Streams
    import fs2.Stream
    import scala.concurrent.duration._
    import sttp.ws.WebSocketFrame
    def randomStream[F[_]]() = endpoint
      .in("tapirrawwebsock")
      .out(webSocketBodyRaw(Fs2Streams[F]))
    // def randomStreamRoute[F[_]: Async]() =
    //  Http4sServerInterpreter[F]().toRoutes(randomStream[F]())(_ =>
    //    Async[F].delay(
    //      (
    //        (_: Stream[F, WebSocketFrame]) => Stream.awakeEvery(2.second).map(x => WebSocketFrame.text(s"tapir raw $x"))
    //      ).asRight
    //    )
    //  )
  object tapirWebsock2:
    import sttp.capabilities.WebSockets
    import sttp.capabilities.fs2.Fs2Streams
    import sttp.tapir._
    import sttp.tapir.server.http4s.Http4sServerInterpreter
    import cats.effect.IO
    import org.http4s.HttpRoutes
    import org.http4s.blaze.server.BlazeServerBuilder
    import org.http4s.server.Router
    import org.http4s.server.websocket.WebSocketBuilder2
    import fs2._
    import scala.concurrent.ExecutionContext

    import fs2.Stream
    import fs2.Pipe
    import scala.concurrent.duration._
    def wsEndpoint[F[_]](): PublicEndpoint[Unit, Unit, Pipe[F, String, String], Fs2Streams[F] with WebSockets] =
      endpoint.get
        .in("tapirwebsock")
        .out(webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.TextPlain](Fs2Streams[F]))

    def myPipe[F[_]: Async](): Pipe[F, String, String] =
      _.evalMap(in => Async[F].delay(println(s"rcv: $in")) >> Async[F].pure(s"noted: $in"))

    def wsRoutes[F[_]: Async](): WebSocketBuilder2[F] => HttpRoutes[F] =
      Http4sServerInterpreter[F]().toWebSocketRoutes(wsEndpoint().serverLogicSuccess[F](_ => Async[F].pure(myPipe())))
