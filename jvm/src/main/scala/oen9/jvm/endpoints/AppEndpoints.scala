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
