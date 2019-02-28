package prv.saevel.scala.trainings.users

case class Address(streetName: String, streetNumber: String, city: Option[String] = None, country: Option[String] = None)
