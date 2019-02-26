package prv.saevel.scala.trainings.generics.higher.kinded.types

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}

trait Monad[F[_]] {

  def pure[X](x: => X): F[X]

  def map[X, Y](context: F[X])(f: X => Y): F[Y]

  def flatMap[X, Y](context: F[X])(f: X => F[Y]): F[Y]
}

object Monad {

  def futureMonad(implicit ex: ExecutionContext): Monad[Future] = new Monad[Future]{

    override def pure[X](x: => X): Future[X] = Future(x)

    override def map[X, Y](context: Future[X])(f: X => Y): Future[Y] = context.map(f)

    override def flatMap[X, Y](context: Future[X])(f: X => Future[Y]): Future[Y] = context.flatMap(f)
  }

  val tryMonad: Monad[Try] = new Monad[Try]{

    override def pure[X](x: => X): Try[X] = Try(x)

    override def map[X, Y](context: Try[X])(f: X => Y): Try[Y] = context.map(f)

    override def flatMap[X, Y](context: Try[X])(f: X => Try[Y]): Try[Y] = context.flatMap(f)
  }
}