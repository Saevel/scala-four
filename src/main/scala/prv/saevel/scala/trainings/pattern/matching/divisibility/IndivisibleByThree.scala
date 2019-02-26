package prv.saevel.scala.trainings.pattern.matching.divisibility

object IndivisibleByThree {

  def unapply(arg: Int): Boolean = arg % 3 != 0
}
