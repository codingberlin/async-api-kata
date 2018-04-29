package api

import javax.inject._
import model.api.ApiUser
import model.frontend.User
import play.api.Configuration
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}
import play.api.Logger

import scala.util.control.NonFatal

@Singleton
class UserApi @Inject()(config: Configuration, ws: WSClient)(
    implicit ex: ExecutionContext) {

  implicit val userReads = Json.reads[ApiUser]
  val apiUrl = config.get[String]("userApiBaseUrl")

  def retrieveUser(userId: Int): Future[User] =
    ws.url(s"$apiUrl/$userId")
      .get()
      .map(_.json.validate[ApiUser])
      .map(
        _.asOpt
          .map(apiUser => User(Some(apiUser.username)))
          .getOrElse(User.empty))
      .recover {
        case NonFatal(throwable) =>
          Logger.error("Could not retrieve user from api", throwable)
          User.empty
      }
}
