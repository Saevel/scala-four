package prv.saevel.scala.trainings.pattern.matching.divisibility

import org.junit.runner.RunWith
import org.scalacheck.Gen
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks

import scala.annotation.tailrec

@RunWith(classOf[JUnitRunner])
class DivisibilityTest extends WordSpec with Matchers with PropertyChecks {

  "Divisibility" when {

    "given a number indivisible by three, but divisible by seven" should {
      "return 1" in forAll(intsDivisibleBySevenButNotThree){ i =>
        Divisibility(i) should equal(1)
      }
    }

    "given a number divisible by both three and seven" should {
      "return 2" in forAll(intsDivisibleByThreeAndSeven){ i =>
        Divisibility(i) should equal(2)
      }
    }

    "given a number indivisible by neither three nor seven" should {
      "return 3" in forAll(intsIndivisibleByThreeOrSeven){ i =>
        Divisibility(i) should equal(3)
      }
    }

    "given a number divisible by three, but indivisible by seven" should {
      "return 4" in forAll(intsDivisibleByThreeButNotSeven){ i =>
        Divisibility(i) should equal(4)
      }
    }
  }

  private val averageSizedInt = Gen.choose(1, 100)

  private val intsDivisibleByThreeAndSeven = averageSizedInt.map(_ * 3 * 7)

  private val intsDivisibleByThreeButNotSeven = averageSizedInt.map(i => indivisibleBy(3 * i, 7))

  private val intsDivisibleBySevenButNotThree = averageSizedInt.map(i => indivisibleBy(7 * i, 3))

  private val intsIndivisibleByThreeOrSeven = averageSizedInt.map(i => indivisibleBy(indivisibleBy(i, 3), 7))

  @tailrec
  private def indivisibleBy(x: Int, y: Int): Int = if( x % y != 0) {
    x
  } else if(x == 0) {
    0
  }
  else {
    indivisibleBy(x / y, y)
  }
}