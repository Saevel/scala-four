package prv.saevel.scala.trainings.pattern.matching.retries

import java.time.LocalDateTime

trait RetryPolicy {

  def shouldRetry(reason: Throwable): Boolean
}

object RetryPolicy {

  def complexRetryPolicy(maxRetries: Long): RetryPolicy = new RetryPolicy {

    private var retries: Seq[RetryAttempt] = Seq.empty

    override def shouldRetry(reason: Throwable): Boolean = if(retries.size > maxRetries) {
      false
    } else reason match {
      case runtime: RuntimeException => {
        retries = retries :+ RetryAttempt(LocalDateTime.now(), reason)
        true
      }
      case _ => false
    }
  }
}
