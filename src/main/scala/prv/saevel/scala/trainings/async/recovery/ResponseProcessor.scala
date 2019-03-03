package prv.saevel.scala.trainings.async.recovery

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, TimeoutException}

object ResponseProcessor {

  def processAsyncResponse(future: Future[(Int, String)])(implicit ex: ExecutionContext, timeout: Duration): (Int, String) =
    try {
      Await.result(future.recover {
        case ConflictException(message) => (422, message)
        case BadRequestException(message) => (400, message)
        case _ => (500, "Internal Server Error")
      }, timeout)
    } catch {
      case e: TimeoutException => (503, e.getMessage)
      case _ => (500, "Internal Server Error")
    }
}
