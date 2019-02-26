package prv.saevel.scala.trainings.interpolators

package object custom {

  implicit class UpperCaseString(context: StringContext){

    def upperCase: String = context.parts.map(_.toUpperCase).fold("")(_+_)

    def lowerCase: String = context.parts.map(_.toLowerCase).fold("")(_+_)
  }
}
