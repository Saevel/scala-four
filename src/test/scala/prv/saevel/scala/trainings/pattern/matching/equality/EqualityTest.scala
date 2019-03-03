package prv.saevel.scala.trainings.pattern.matching.equality

import org.junit.runner.RunWith
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks

@RunWith(classOf[JUnitRunner])
class EqualityTest extends WordSpec with Matchers with PropertyChecks {

  "intEquality" when {
    "comparing an Int to a non-Int" should {
      "always return false" in forAll(arbitrary[Int], nonInts){ (i, x) =>
        Equality.intEquality.areEqual(i, x) should be(false)
      }
    }

    "comparing any Int to itself" should {
      "always return true" in forAll(arbitrary[Int]){ i =>
        Equality.intEquality.areEqual(i, i) should be(true)
      }
    }
  }

  "tolerantDoubleEquality" when {

    "comparing double to a non-double" should {
      "always return false" in forAll(arbitrary[Double], nonDoubles){ (x, y) =>
        Equality.tolerantDoubleEquality.areEqual(x, y) should be(false)
      }
    }

    "comparing two doubles separated by less than 0.01" should {
      "return true" in forAll(nearlyEqualDoubles){ case (x, y) =>
        Equality.tolerantDoubleEquality.areEqual(x, y) should be(true)
      }
    }

    "comparing any two doubles" should {
      "provide the same results no matter the order" in forAll(arbitrary[Double], arbitrary[Double]){ (x, y) =>
        Equality.tolerantDoubleEquality.areEqual(x, y) should equal(Equality.tolerantDoubleEquality.areEqual(y, x))
      }
    }

    "comparing two doubles separated by more than 0.01" should {
      "return false" in forAll(nonEqualDoubles){ case (x, y) =>
        Equality.tolerantDoubleEquality.areEqual(x, y) should be(false)
      }
    }
  }

  "floatEquality" when {

    "comparing a Float to neither Float nor Double" should {
      "always return false" in forAll(arbitrary[Float], neitherFloatNorDouble){ (x, y) =>
        Equality.floatEquality.areEqual(x, y) should be(false)
      }
    }

    "comparing a Float to itself" should {
      "always return true" in forAll(arbitrary[Float]){ x =>
        Equality.floatEquality.areEqual(x, x) should be(true)
      }
    }

    "comparing two Floats closer to each other than 0.01" should {
      "return true" in forAll(nearlyEqualFloats){ case (x, y) =>
        Equality.floatEquality.areEqual(x, y) should be(true)
      }
    }

    "comparing two Floats further from each other than 0.01" should {
      "return false" in forAll(nonEqualFloats){ case (x, y) =>
          Equality.floatEquality.areEqual(x, y) should be(false)
      }
    }

    "comparing a Float to itself cast to Double" should {
      "return true" in forAll(arbitrary[Float]){ x =>
        Equality.floatEquality.areEqual(x, x.toDouble) should be(true)
      }
    }

    "comparing a Float to a Double less distant from it than 0.01" should {
      "return true" in forAll(nearlyEqualsFloatsAndDoubles){ case (x, y) =>
          Equality.floatEquality.areEqual(x, y) should be(true)
      }
    }

    "comparing a Float to Double more distant from it than 0.01" should {
      "return false" in forAll(nonEqualFloatsAndDoubles){ case (x, y) =>
        Equality.floatEquality.areEqual(x, y) should be(false)
      }
    }
  }

  private val nearlyEqualDoubles: Gen[(Double, Double)] = for {
    x <- arbitrary[Double]
    plus <- arbitrary[Boolean]
    y <- if(plus) Gen.choose(x, x + 0.01) else Gen.choose(x - 0.01, x)
  } yield (x, y)

  private val nonEqualDoubles: Gen[(Double, Double)] = for {
    x <- arbitrary[Double]
    plus <- arbitrary[Boolean]
    delta <- Gen.choose(0.01, 1000.0).map(Math.abs(_))
    y <- if(plus) Gen.choose(x + delta, Double.PositiveInfinity) else Gen.choose(Double.NegativeInfinity, x - delta)
  } yield (x, y)

  private val nearlyEqualFloats: Gen[(Float, Float)] = for {
    x <- arbitrary[Float]
    plus <- arbitrary[Boolean]
    y <- if(plus) Gen.choose(x, x + 0.01f) else Gen.choose(x - 0.01f, x)
  } yield (x, y)

  private val nonEqualFloats: Gen[(Float, Float)] = for {
    x <- arbitrary[Float]
    plus <- arbitrary[Boolean]
    delta <- Gen.choose(0.01f, 1000.0f).map(Math.abs(_))
    y <- if(plus) Gen.choose(x + delta, Float.PositiveInfinity) else Gen.choose(Float.NegativeInfinity, x - delta)
  } yield (x, y)

  private val nearlyEqualsFloatsAndDoubles: Gen[(Float, Double)] = nearlyEqualFloats.map{ case (x, y) => (x, y.toDouble)}

  private val nonEqualFloatsAndDoubles: Gen[(Float, Double)] = nonEqualFloats.map{ case ( x, y) => (x, y.toDouble)}

  private val nonInts = Gen.oneOf(
    arbitrary[Double],
    arbitrary[Float],
    arbitrary[String],
    arbitrary[Byte],
    arbitrary[Short],
    arbitrary[Long],
    arbitrary[Boolean]
  )

  private val nonDoubles = Gen.oneOf(
    arbitrary[Int],
    arbitrary[Float],
    arbitrary[String],
    arbitrary[Byte],
    arbitrary[Short],
    arbitrary[Long],
    arbitrary[Boolean]
  )

  private val neitherFloatNorDouble = Gen.oneOf(
    arbitrary[Int],
    arbitrary[String],
    arbitrary[Byte],
    arbitrary[Short],
    arbitrary[Long],
    arbitrary[Boolean]
  )
}
