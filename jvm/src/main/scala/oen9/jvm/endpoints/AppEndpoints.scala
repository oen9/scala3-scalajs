package oen9.jvm.endpoints

import cats.effect.Sync
import cats.implicits.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import oen9.jvm.Model.*
import org.http4s.HttpRoutes
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl

import java.net.InetAddress

object AppEndpoints:
  def endpoints[F[_]: Sync](): HttpRoutes[F] =
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "hostname" =>
        for {
          hostname <- Sync[F].delay(InetAddress.getLocalHost.getHostName)
          srvInfo = SrvInfo(hostname = hostname)
          result <- Ok(srvInfo.asJson)
        } yield result

      case GET -> Root / "hello" / name =>
        Ok(s"Hello, $name.")
    }
