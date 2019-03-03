package prv.saevel.scala.trainings.generics.higher.kinded.types

import prv.saevel.scala.trainings.users.{ContactData, User}

import scala.concurrent.Future
import scala.util.Try

class Processor {

  def processUserAsync(user: User)
                      (implicit monad: Monad[Future],
                       monadError: MonadError[Future]): Future[(Int, String)] =
    processUser[Future](user)

  def processUserSync(user: User)
                     (implicit monad: Monad[Try],
                      monadError: MonadError[Try]): Try[(Int, String)] =
    processUser[Try](user)

  def processUser[F[_]](user: User)(implicit monad: Monad[F], monadError: MonadError[F]): F[(Int, String)] =
    monad.flatMap(monad.pure(hasUniqueId(user))){ _ =>
      monad.flatMap(monad.pure(hasPositiveAge(user))){ _ =>
        monadError.handleError(
          monad.map(monad.pure(hasAnyAddress(user.contactData))){ _ => (200, "OK")}
        ){
          case nce @ NoContactException => (409, nce.getMessage)
          case nae @ NegativeAgeException(_) => (409, nae.getMessage)
          case _ => (500, "Internal Server Error")
        }
      }
    }

  def hasAnyAddress(data: ContactData): ContactData = data match {
    case ContactData(Some(_), _, _) | ContactData(_, Some(_), _) | ContactData(_, _, Some(_)) => data
    case _ => throw NoContactException
  }

  def hasUniqueId(user: User): User = user

  def hasPositiveAge(user: User): User = user.personalData.age match {
    case None => user
    case  Some(x) if(x > 0) => user
    case Some(age) => throw NegativeAgeException(age)
  }
}