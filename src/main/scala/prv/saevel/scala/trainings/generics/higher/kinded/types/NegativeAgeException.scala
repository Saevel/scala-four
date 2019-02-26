package prv.saevel.scala.trainings.generics.higher.kinded.types

case class NegativeAgeException(age: Int) extends IllegalArgumentException(s"User has negative age: $age")
