package prv.saevel.scala.trainings.pattern.matching.retries

import scala.util.{Failure, Success, Try}

object Retry {

  def apply[X](f: => X)(implicit retryPolicy: RetryPolicy): Try[X] = Try(f) match {
    case s @ Success(_) => s
    case failure @ Failure(e) => if(retryPolicy.shouldRetry(e)){
      apply(f)(retryPolicy)
    } else {
      failure
    }
  }
}
