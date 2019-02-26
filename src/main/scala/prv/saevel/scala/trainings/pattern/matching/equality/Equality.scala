package prv.saevel.scala.trainings.pattern.matching.equality

trait Equality[T] {

  def areEqual(x: T, y: Any): Boolean
}

object Equality {

  implicit val intEquality: Equality[Int] = new Equality[Int] {
    override def areEqual(x: Int, y: Any): Boolean = y match {
      case i: Int => (x == i)
      case _ => false
    }
  }

  implicit val tolerantDoubleEquality: Equality[Double] = new Equality[Double] {
    override def areEqual(x: Double, y: Any): Boolean = y match {
      case d: Double => Math.abs(d - x) <= 0.01
      case _ => false
    }
  }

  implicit val floatEquality: Equality[Float] = new Equality[Float] {
    override def areEqual(x: Float, y: Any): Boolean = x match {
      case f: Float => Math.abs(f - x) <= 0.01
      case d: Double => (d > Float.MaxValue) && (Math.abs(d - x.toDouble) <= 0.01)
    }
  }
}