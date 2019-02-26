package prv.saevel.scala.trainings.pattern.matching.divisibility

object Divisibility {

  def apply(i: Int): Int = i match {
    case IndivisibleByThree() & DivisibleBySeven() => 1
    case DivisibleBySeven() => 2
    case IndivisibleByThree() => 3
    case _ => 4
  }
}


