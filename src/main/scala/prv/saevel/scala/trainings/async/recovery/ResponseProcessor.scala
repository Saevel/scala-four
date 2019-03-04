package prv.saevel.scala.trainings.async.recovery

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, TimeoutException}

object ResponseProcessor {

  def processAsyncResponse(future: Future[(Int, String)])
                          (implicit ex: ExecutionContext, timeout: Duration): (Int, String) = ???
}