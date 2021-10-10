package oen9.jvm.endpoints

import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.server.websocket.WebSocketBuilder
import cats.effect.Async
import fs2.Stream
import fs2.Pipe
import org.http4s.websocket.WebSocketFrame
import scala.concurrent.duration._
import org.http4s.server.websocket.WebSocketBuilder2

object WebsockEndpoints:
  def routes[F[_]: Async](websockBuilder: WebSocketBuilder2[F]): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of { case request @ GET -> Root / "websock" =>
      websockBuilder
        .withOnClose(Async[F].delay(println("websock closed")))
        .build(
          send = Stream.awakeEvery(2.second).map(x => WebSocketFrame.Text(s"foo $x")),
          receive = _.map(_ => ())
        )
    }
  }
