package prv.saevel.scala.trainings.async.chaining

import prv.saevel.scala.trainings.accounts.{Account, Transaction}

import scala.concurrent.{ExecutionContext, Future}

class PersonalFinancesService(accountRepository: AccountRepository,
                              transactionRepository: TransactionRepository)
                             (implicit ex: ExecutionContext) {

  def checkBalanceForUser(userId: Long, accuracy: Double): Future[Seq[AccountBalanceError]] =
    accountRepository
      .findAccountsByUserId(userId)
      .flatMap(accounts =>
        Future
          .sequence(accounts.map(toIdAndBalances))
          .map(x => x.filter(byBalanceInequality(accuracy)))
          .map(x => x.map(toAccountBalanceError))
      )

  def transactionsBalance(incoming: Seq[Transaction], outgoing: Seq[Transaction]): Double =
    incoming.map(_.amount).fold(0.0)(_ + _) - outgoing.map(_.amount).fold(0.0)(_ + _)

  private def toAccountBalanceError(data: (Long, Double, Double)): AccountBalanceError = data match {
    case (id, balance, transactionsBalance) => AccountBalanceError(id, balance, transactionsBalance)
  }

  private def byBalanceInequality(accuracy: Double)(data: (Long, Double, Double)): Boolean = data match {
    case (_, balance, transactionsBalance) => (Math.abs(balance - transactionsBalance) >= accuracy)
  }

  private def toIdAndBalances(account: Account): Future[(Long, Double, Double)] =
    transactionRepository.findTransactionByTargetAccount(account.id).flatMap(incoming =>
      transactionRepository.findTransactionsBySourceAccount(account.id).map(outgoing =>
        (account.id, account.balance, transactionsBalance(incoming, outgoing))
      )
    )
}
