package oen9.jvm

import cats.effect.Async
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.*
import oen9.jvm.endpoints.AppEndpoints
import oen9.jvm.endpoints.StaticEndpoints
import oen9.jvm.endpoints.WebsockEndpoints
import org.http4s.HttpApp
import org.http4s.implicits.*
import org.http4s.server.websocket.WebSocketBuilder2
import org.typelevel.log4cats.Logger
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.Server
import sttp.tapir.openapi.circe.yaml.*
import sttp.tapir.redoc.Redoc
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.SwaggerUI

object Main extends IOApp, Logging:
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _   <- Logger[IO].info("App started")
      cfg <- AppConfig.load[IO]()
      _   <- app[IO](cfg)
    } yield ExitCode.Success

  def app[F[_]: Async](cfg: AppConfig): F[Unit] =
    import org.http4s.server.Router
    import org.http4s.implicits.*

    val yaml = OpenAPIDocsInterpreter()
      .toOpenAPI(AppEndpoints.endpoints, "scala3-scalajs", "0.0.1")
      .servers(
        List(
          Server("http://localhost:8080").description("local server"),
          Server("https://scala3-scalajs.herokuapp.com").description("Production server")
        )
      )
      .toYaml

    def httpApp(websockBuilder: WebSocketBuilder2[F]) = (
      AppEndpoints.routes() <+>
        AppEndpoints.tapirWebsock.randomStreamRoute() <+>
        AppEndpoints.tapirRawWebsock.randomStreamRoute() <+>
        WebsockEndpoints.routes(websockBuilder) <+>
        StaticEndpoints.endpoints(cfg.assets) <+>
        Http4sServerInterpreter().toRoutes(SwaggerUI(yaml = yaml)) <+>
        Http4sServerInterpreter().toRoutes(Redoc(title = "scala3-scalajs", yaml = yaml, prefix = List("redoc")))
    ).orNotFound

    startEmber(httpApp)
  //startBlaze(httpApp)

  def startEmber[F[_]: Async](httpApp: WebSocketBuilder2[F] => HttpApp[F]): F[Unit] =
    import org.http4s.ember.server._
    import com.comcast.ip4s.port
    import com.comcast.ip4s.Port
    import com.comcast.ip4s.Host
    for {
      _   <- Logger[F].trace("Starting http4s ember app")
      ec  <- Async[F].executionContext
      cfg <- AppConfig.load()
      _ <- EmberServerBuilder
        .default[F]
        .withHostOption(Host.fromString(cfg.http.host))
        .withPort(Port.fromInt(cfg.http.port).getOrElse(port"8080"))
        .withHttpWebSocketApp(httpApp)
        .build
        .useForever
    } yield ()

  def startBlaze[F[_]: Async](httpApp: WebSocketBuilder2[F] => HttpApp[F]): F[Unit] =
    import org.http4s.blaze.server._
    for {
      _   <- Logger[F].trace("Starting http4s blaze app")
      cfg <- AppConfig.load()
      _ <- BlazeServerBuilder[F]
        .bindHttp(cfg.http.port, cfg.http.host)
        .withHttpWebSocketApp(httpApp)
        .resource
        .useForever
    } yield ()

def msg = "I was compiled by Scala 3. :)"
