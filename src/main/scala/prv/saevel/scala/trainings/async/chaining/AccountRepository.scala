package prv.saevel.scala.trainings.async.chaining

import prv.saevel.scala.trainings.accounts.Account

import scala.concurrent.Future

trait AccountRepository {

  def findAccountsByUserId(userId: Long): Future[List[Account]]
}
