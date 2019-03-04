package prv.saevel.scala.trainings.pattern.matching.equality

trait Equality[T] {

  def areEqual(x: T, y: Any): Boolean
}

object Equality {

  val intEquality: Equality[Int] = ???

  val tolerantDoubleEquality: Equality[Double] = ???

  val floatEquality: Equality[Float] = ???
}