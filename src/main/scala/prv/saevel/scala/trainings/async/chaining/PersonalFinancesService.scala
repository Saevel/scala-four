package prv.saevel.scala.trainings.async.chaining

import scala.concurrent.{ExecutionContext, Future}

class PersonalFinancesService(accountRepository: AccountRepository,
                              transactionRepository: TransactionRepository)
                             (implicit ex: ExecutionContext) {

  def checkBalanceForUser(userId: Long, accuracy: Double): Future[Seq[AccountBalanceError]] = ???
}
