package prv.saevel.scala.trainings.pattern.matching.retries

import scala.util.{Failure, Success, Try}

object Retry {

  def apply[X](f: => X)(implicit retryPolicy: RetryPolicy): Try[X] = ???
}
