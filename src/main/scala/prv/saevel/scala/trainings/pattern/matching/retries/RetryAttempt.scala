package prv.saevel.scala.trainings.pattern.matching.retries

import java.time.LocalDateTime

case class RetryAttempt(timestamp: LocalDateTime, reason: Throwable)
