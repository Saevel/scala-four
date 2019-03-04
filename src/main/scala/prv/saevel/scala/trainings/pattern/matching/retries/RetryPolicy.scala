package prv.saevel.scala.trainings.pattern.matching.retries

trait RetryPolicy {

  def shouldRetry(reason: Throwable): Boolean
}

object RetryPolicy {

  def complexRetryPolicy(maxRetries: Long): RetryPolicy = ???
}
