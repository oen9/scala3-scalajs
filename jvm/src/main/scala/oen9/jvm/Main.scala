package oen9.jvm

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.*
import org.typelevel.log4cats.Logger
import cats.effect.Async
import oen9.jvm.endpoints.AppEndpoints
import oen9.jvm.endpoints.StaticEndpoints
import org.http4s.implicits.*

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

    val httpApp = (
      AppEndpoints.endpoints() <+>
        StaticEndpoints.endpoints(cfg.assets)
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
