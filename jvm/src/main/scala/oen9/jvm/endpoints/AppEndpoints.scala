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

  def checkRoute[F[_]: Async]() = Http4sServerInterpreter[F]().toRoutes(check)(_ => Async[F].pure("ok".asRight))
  def hostnameRoute[F[_]: Async]() = Http4sServerInterpreter[F]().toRoutes(hostname)(_ =>
    for {
      hostname <- Async[F].delay(InetAddress.getLocalHost.getHostName)
    } yield SrvInfo(hostname = hostname).asRight
  )

  def routes[F[_]: Async]() = checkRoute() <+>
    hostnameRoute()
  def endpoints = List(check, hostname)

  // this doesn't work, no websock upgrade (Unknown connection header: 'Upgrade'. Closing connection upon completion.)
  object tapirWebsock:
    import sttp.capabilities.fs2.Fs2Streams
    import fs2.Stream
    import scala.concurrent.duration._
    def randomStream[F[_]]() = endpoint
      .in("tapirwebsock")
      .out(webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.Json](Fs2Streams[F]))
    def randomStreamRoute[F[_]: Async]() =
      Http4sServerInterpreter[F]().toRoutes(randomStream[F]())(_ =>
        Async[F].delay(((_: Stream[F, String]) => Stream.awakeEvery(2.second).map(x => s"tapir $x")).asRight)
      )
  object tapirRawWebsock:
    import sttp.capabilities.fs2.Fs2Streams
    import fs2.Stream
    import scala.concurrent.duration._
    import sttp.ws.WebSocketFrame
    def randomStream[F[_]]() = endpoint
      .in("tapirrawwebsock")
      .out(webSocketBodyRaw(Fs2Streams[F]))
    def randomStreamRoute[F[_]: Async]() =
      Http4sServerInterpreter[F]().toRoutes(randomStream[F]())(_ =>
        Async[F].delay(
          (
            (_: Stream[F, WebSocketFrame]) => Stream.awakeEvery(2.second).map(x => WebSocketFrame.text(s"tapir raw $x"))
          ).asRight
        )
      )
