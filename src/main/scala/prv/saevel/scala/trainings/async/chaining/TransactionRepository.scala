package prv.saevel.scala.trainings.async.chaining

import prv.saevel.scala.trainings.accounts.Transaction

import scala.concurrent.Future

trait TransactionRepository {

  def findTransactionsBySourceAccount(accountId: Long): Future[List[Transaction]]

  def findTransactionByTargetAccount(accountId: Long): Future[List[Transaction]]
}
