package prv.saevel.scala.trainings.async.chaining

import java.io.IOException
import java.time.LocalDateTime
import java.util

import org.junit.runner.RunWith
import org.scalacheck.Gen
import org.scalacheck.Arbitrary._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks
import prv.saevel.scala.trainings.accounts.{Account, Transaction, TransactionType}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class PersonalFinancesServiceTest extends WordSpec with Matchers with PropertyChecks with ScalaFutures {


  "PersonalFinancesService" when {

    "fetching an account by person id fails" should {

      "fail with the same cause" in forAll(throwables, throwables, arbitrary[Long], arbitrary[Double]){ (accountException, otherException, anyId, anyAccuracy) =>

        val service = new PersonalFinancesService(
          failingAccountRepository(accountException),
            failingTransactionRepository(otherException, otherException)
        )

        service.checkBalanceForUser(anyId, anyAccuracy).failed.futureValue should be(accountException)
      }
    }

    "fetching account succeeds but fetching transactions fails" should {

      "fail with the same cause" in forAll(throwables, throwables, arbitrary[Long], arbitrary[Double]){ (accountException, transactionException, anyId, anyAccuracy) =>
        val service = new PersonalFinancesService(
          failingAccountRepository(accountException),
          failingTransactionRepository(transactionException, transactionException)
        )

        service.checkBalanceForUser(anyId, anyAccuracy).failed.futureValue should be(transactionException)
      }
    }

    "provided a list of accounts and related transactions" should {

      "report errors for incorrect account balances" in forAll(validAccounts, invalidAccounts){ (valid, invalid) =>

      }
    }
  }

  private val throwables = Gen.oneOf(
    new IllegalArgumentException("Thrown on purpose"),
    new IllegalStateException("Thrown on purpose"),
    new RuntimeException("Thrown on purpose"),
    new IOException("Thrown on purpose")
  )

  private val ids = Gen.choose(0L, 10000L)

  private val smallInts = Gen.choose(1, 10)

  private val validAccounts: Gen[(Long, List[(Account, List[Transaction])])] = for {
    userId <- ids
    accounts <- accounts(userId, true)
  } yield (userId, accounts)

  private val invalidAccounts: Gen[(Long, List[(Account, List[Transaction])])] = for {
    userId <- ids
    accounts <- accounts(userId, false)
  } yield (userId, accounts)

  private def accounts(userId: Long, valid: Boolean): Gen[List[(Account, List[Transaction])]] = smallInts.flatMap(n =>
    Gen.listOfN(n, for {
      accountId <- ids
      transactions <- transactions(accountId)
      balance <- if(valid) Gen.const(balance(transactions)) else Gen.choose(0.0, 500000.0)
    } yield (Account(accountId, userId, if(valid) balance(transactions)), transactions)))

  private def account(userId: Long, balance: Double): Gen[Account] = ids.map(id =>
    Account(id, userId, balance)
  )

  private def transactions(accountId: Long): Gen[List[Transaction]] = smallInts.flatMap(n => Gen.listOfN(n, for {
    kind <- Gen.oneOf(TransactionType.values.toSeq)
  } yield if(kind == TransactionType.Withdrawal) withdrawal(accountId) else insertion(accountId)))

  private def withdrawal(sourceId: Long): Gen[Transaction] = for {
    amount <- Gen.choose(0.0, 5000.0)
    id <- ids
  } yield Transaction(id, LocalDateTime.now, TransactionType.Withdrawal, amount, Some(sourceId), None)

  private def insertion(targetId: Long): Gen[Transaction] = for {
    amount <- Gen.choose(0.0, 5000.0)
    id <- ids
  } yield Transaction(id, LocalDateTime.now, TransactionType.Insertion, amount, None, Some(targetId))

  private def failingAccountRepository(t: Throwable) = new AccountRepository {
    override def findAccountsByUserId(userId: Long): Future[List[Account]] = Future.failed(t)
  }

  private def failingTransactionRepository(sourceException: Throwable, targetException: Throwable) = new TransactionRepository {
    override def findTransactionsBySourceAccount(accountId: Long): Future[List[Transaction]] = Future.failed(sourceException)
    override def findTransactionByTargetAccount(accountId: Long): Future[List[Transaction]] = Future.failed(targetException)
  }

  private def balance(transactions: Seq[Transaction]): Double = transactions.map{ transaction =>
    if(transaction.kind == TransactionType.Withdrawal) (-1.0) * transaction.amount else transaction.amount
  }.fold(0.0)(_+_)
}
