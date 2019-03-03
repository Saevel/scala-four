package prv.saevel.scala.trainings.async.recovery

import org.junit.runner.RunWith
import org.scalacheck.Gen
import org.scalacheck.Arbitrary._
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class ResponseProcessorTest extends WordSpec with Matchers with PropertyChecks {

  "processAsyncResponse" when {

    "given a Future that evaluates successfully to a (Int, String) tuple" should {
      "return that very (Int, String) tuple" in forAll(statusAndTextPairs){ pair =>
        ResponseProcessor.processAsyncResponse(Future.successful(pair)) should equal(pair)
      }
    }

    "given a Future that fails with a ConflictException(message)" should {
      "return the status 422 and the given message" in forAll(conflictExceptions){ exception =>
        ResponseProcessor.processAsyncResponse(Future.failed(exception)) should equal (422, exception.message)
      }
    }

    "given a Future that fails with a BadRequestException(message)" should {
      "return the status 400 and the given message" in forAll(badRequestExceptions){ exception =>
        ResponseProcessor.processAsyncResponse(Future.failed(exception)) should equal (400, exception.message)
      }
    }

    "given a Future that fails with an unrecognized exception" should {
      "return the status 500 with the 'Internal Server Error' message" in forAll(unrecognizedRuntimeExceptions){ exception =>
        ResponseProcessor.processAsyncResponse(Future.failed(exception)) should equal (500, "Internal Server Error")
      }
    }

    "given a Future that doesn't finish in a time given by the implicit timeout" should {
      "return the status code 503" in forAll(longEvaluationTimes, arbitrary[Int], arbitrary[String]){ (evalTime, anyStatus, anyMessage) =>
        val unresponsiveFuture: Future[(Int, String)] = Future {
          Thread.sleep(evalTime)
          (anyStatus, anyMessage)
        }
        ResponseProcessor.processAsyncResponse(unresponsiveFuture)._1 should equal(503)
      }
    }
  }

  import scala.concurrent.duration._

  private implicit val httpTimeout: Duration = 1 second

  private val longEvaluationTimes: Gen[Long] = Gen.choose(100, 1000).map(delta => httpTimeout.toMillis + delta)

  private val statusAndTextPairs: Gen[(Int, String)] = for {
    status <- Gen.choose(200, 299)
    text <- Gen.alphaStr
  } yield (status, text)

  private val conflictExceptions: Gen[ConflictException] = Gen.alphaStr.map(ConflictException(_))

  private val badRequestExceptions: Gen[BadRequestException] = Gen.alphaStr.map(BadRequestException(_))

  private val unrecognizedRuntimeExceptions: Gen[Throwable] = Gen.oneOf(
    new IllegalArgumentException("Thrown on purpose"),
    new IllegalStateException("Thrown on purpose"),
    new ArithmeticException("Thrown on purpose")
  )
}
