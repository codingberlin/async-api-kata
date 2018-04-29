package model.frontend

case class User(name: Option[String])

object User {
  val empty = User(None)
}
