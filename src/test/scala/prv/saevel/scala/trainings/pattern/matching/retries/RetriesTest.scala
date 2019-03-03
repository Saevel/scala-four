package prv.saevel.scala.trainings.pattern.matching.retries

import org.junit.runner.RunWith
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen
import org.scalatest.{Matchers, TryValues, WordSpec}
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks

@RunWith(classOf[JUnitRunner])
class RetriesTest extends WordSpec with Matchers with PropertyChecks with TryValues {

  "Retries" when {

    "provided with a successful task" should {

      "never retry, no matter the retry policy" in forAll(arbitrary[Int]) { i =>
        implicit val policy: RetryPolicy = alwaysRetry
        var runCount: Int = 0
        val result = Retry{
          runCount = runCount + 1
          i
        }

        withClue("Should return the value provided by the task"){
          result.success.value should be(i)
        }

        withClue("Should run only once"){
          runCount should be(1)
        }
      }
    }

    "provided with a failing task and a retry policy" should {
      "retry until the policy says so" in forAll(smallInts){ maxRetries =>
        implicit val policy = retryNTimes(maxRetries)
        var runCount: Int = 0
        val result = Retry {
          runCount = runCount + 1
          throw new IllegalArgumentException("Whatever")
        }

        withClue("Task should eventually fail"){
          result.isFailure should be(true)
        }

        withClue("Task should be retired as ordered"){
          runCount should be(maxRetries + 1)
        }
      }
    }

    "provided with a task that initially fails but eventually succeeds and an always retrying strategy" should {
      "retry until eventual success" in forAll(smallInts, arbitrary[Int]){ (maxFailures, i) =>
        implicit val policy = alwaysRetry
        var runCount: Int = 0
        val result = Retry {
          runCount = runCount + 1
          if(runCount <= maxFailures) {
            throw new IllegalArgumentException("Whtever")
          } else i
        }

        withClue("Should eventually return the given value"){
          result.success.value should equal(i)
        }

        withClue("Should retry until the task succeeds"){
          runCount should equal(maxFailures + 1)
        }
      }
    }
  }

  private val smallInts: Gen[Int] = Gen.choose(1, 10)

  val alwaysRetry = new RetryPolicy {
    override def shouldRetry(reason: Throwable): Boolean = true
  }

  def retryNTimes(maxRetries: Int) = new RetryPolicy {

    private var runCount: Int = 0

    override def shouldRetry(reason: Throwable): Boolean = {
      runCount = runCount + 1
      (runCount <= maxRetries)
    }
  }
}
