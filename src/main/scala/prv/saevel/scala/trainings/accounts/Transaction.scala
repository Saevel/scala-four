package prv.saevel.scala.trainings.accounts

import java.time.LocalDateTime

case class Transaction(id: Long,
                       timestamp: LocalDateTime,
                       kind: TransactionType.Value,
                       amount: Double,
                       sourceAccountId: Option[Long],
                       targetAccountId: Option[Long])