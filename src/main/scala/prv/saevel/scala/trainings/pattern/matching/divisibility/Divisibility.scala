package prv.saevel.scala.trainings.pattern.matching.divisibility

object Divisibility {

  def apply(i: Int): Int = i match {
    case IndivisibleByThree() => i match {
      case DivisibleBySeven() => 1
      case IndivisibleByThree() => 3
    }
    case DivisibleBySeven() => 2
    case _ => 4
  }
}


