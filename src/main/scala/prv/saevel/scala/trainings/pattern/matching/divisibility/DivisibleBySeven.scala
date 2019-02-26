package prv.saevel.scala.trainings.pattern.matching.divisibility

object DivisibleBySeven {

  def unapply(i: Int): Boolean = i % 7 != 0
}
