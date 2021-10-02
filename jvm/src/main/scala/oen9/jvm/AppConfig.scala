package oen9.jvm

import cats.effect.Sync
import AppConfig._
import com.typesafe.config.ConfigFactory

case class AppConfig(http: Http, assets: String)

object AppConfig: // waiting for pureconfig to support scala
  case class Http(port: Int, host: String)

  def load[F[_]: Sync](): F[AppConfig] = Sync[F].delay {
    val cfg     = ConfigFactory.load()
    val httpCfg = cfg.getConfig("http")
    val http    = Http(port = httpCfg.getInt("port"), host = httpCfg.getString("host"))
    val assets  = cfg.getString("assets")
    AppConfig(http, assets)
  }
