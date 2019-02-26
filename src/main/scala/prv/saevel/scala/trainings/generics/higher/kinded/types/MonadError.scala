package prv.saevel.scala.trainings.generics.higher.kinded.types

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait MonadError[Context[_]] {

  def success[Content](x: Content): Context[Content]

  def failure[Content](e: Throwable): Context[Content]

  def handleError[Content](context: Context[Content])(f: Throwable => Content): Context[Content]
}

object MonadError {

  def futureMonadError(implicit ex: ExecutionContext): MonadError[Future] = new MonadError[Future]{
    override def success[Content](x: Content): Future[Content] = Future.successful(x)

    override def failure[Content](e: Throwable): Future[Content] = Future.failed(e)

    override def handleError[Content](context: Future[Content])(f: Throwable => Content): Future[Content] =
      context.recover{ case t => f(t)}
  }

  val tryMonadError: MonadError[Try] = new MonadError[Try] {
    override def success[Content](x: Content): Try[Content] = Success(x)

    override def failure[Content](e: Throwable): Try[Content] = Failure[Content](e)

    override def handleError[Content](context: Try[Content])(f: Throwable => Content): Try[Content] =
      context.recover{case e => f(e)}
  }
}
