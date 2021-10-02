package oen9.jvm

import cats.effect.kernel.Sync
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait Logging {
  given [F[_]: Sync]: SelfAwareStructuredLogger[F] = Slf4jLogger.getLoggerFromClass(getClass)
}
