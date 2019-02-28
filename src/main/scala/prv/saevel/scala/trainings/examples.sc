import prv.saevel.scala.trainings.users.{Address, ContactData, User}
import prv.saevel.scala.trainings.{Address, PersonalData, User}

import scala.annotation.tailrec
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

val i = 10;

val doAsync: Future[Int] = Future {
  Math.pow(i, i).toInt
}


val doFurther: Future[Double] = doAsync.map(i => i / 17.0)

trait AsyncUserRepository {

  def getUser(id: Long): Future[User]
}


trait AsyncContactDataRepository {

  def findContactDataForUser(user: User): Future[ContactData]
}

val userRepo: AsyncUserRepository = ???

val contactDataRepo: AsyncContactDataRepository = ???

def findContactDataByUserId(id: Long): Future[ContactData] = userRepo.getUser(123).flatMap(user =>
  contactDataRepo.findContactDataForUser(user)
)

def recognizeType(x: Any): String = x match {
  case i: Int => "Integer"
  case l: Long => "Long"
  case d: Double => "Double"
  case s: String => "String"
  case e: Exception => "Exception"
  case other => "Other"
}

object Even {
  def unapply(i: Int): Boolean = i % 2 == 0
}

object Odd {
  def unapply(arg: Int): Boolean = i % 2 != 0
}

val ints: List[Int] = ???

val evenNumbers = ints.filter{
  case Even() => true
  case Odd() => false
}

def adultAgeAndAddress(user: User): Option[(Int, Address)] = user match {

  case User(_, PersonalData(_, _, Some(age)), ContactData(_, _, Some(address))) if(age >= 18) => Some((age, address))

  case otherwise => None
}

object UserWithAnyContact {

  def unapply(user: User): Option[String] = user.contactData match {
    case ContactData(Some(phoneNumber), _, _) => Some("tel:" + phoneNumber)
    case ContactData(_, Some(email), _) => Some("mailto:" + email)
    case ContactData(_, _, Some(address)) => Some("letterTo:" + address.toString)
    case otherwise => None
  }
}

object NoContactException extends IllegalStateException("User does not have any contact on him")

def contactUser(url: String): Future[Boolean] = Future.successful(true)

def contactUser(user: User): Future[Boolean] = user match {
  case UserWithAnyContact(contactUrl) => contactUser(contactUrl)
  case otherwise => Future.failed(NoContactException)
}


def factorial(i: Int): Option[Int] = i match {
  case 0 => Some(1)
  case negativeNumber => None
  case n if(n > 0) => factorial(n - 1).map(_ * n)
}

def getFirst[T](data: List[T]): Option[T] = data match {
  case first :: rest => Some(first)
  case otherwise => None
}

def getThirdAndLast[T](data: List[T]): Option[T] = data match {
  case List(_, _, third) => Some(third)
  case otherwise => None
}

def evilJavaDivision(x: Double, y: Double): Double = x / y

def niceScalaDivision(x: Double, y: Double): Option[Double] = try {
  Some(evilJavaDivision(x, y))
} catch {
  case a: ArithmeticException => None
}

