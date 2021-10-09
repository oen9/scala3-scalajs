package oen9.jvm

import cats.effect.Async
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.*
import oen9.jvm.endpoints.AppEndpoints
import oen9.jvm.endpoints.StaticEndpoints
import org.http4s.implicits.*
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

    val httpApp = (
      AppEndpoints.routes() <+>
        StaticEndpoints.endpoints(cfg.assets) <+>
        Http4sServerInterpreter().toRoutes(SwaggerUI(yaml = yaml)) <+>
        Http4sServerInterpreter().toRoutes(Redoc(title = "scala3-scalajs", yaml = yaml, prefix = List("redoc")))
    ).orNotFound

    import org.http4s.blaze.server._
    for {
      _   <- Logger[F].trace("Starting http4s app")
      ec  <- Async[F].executionContext
      cfg <- AppConfig.load()
      _ <- BlazeServerBuilder[F](ec)
        .bindHttp(cfg.http.port, cfg.http.host)
        .withHttpApp(httpApp)
        .resource
        .use(_ => Async[F].never)
    } yield ()

def msg = "I was compiled by Scala 3. :)"
