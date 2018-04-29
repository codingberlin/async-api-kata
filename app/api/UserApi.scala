package api

import javax.inject._
import model.api.ApiUser
import play.api.Configuration
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserApi @Inject()(config: Configuration, ws: WSClient)(
    implicit ex: ExecutionContext) {

  implicit val userReads = Json.reads[ApiUser]
  val apiUrl = config.get[String]("userApiBaseUrl")

  def retrieveUser(userId: Int): Future[Option[ApiUser]] =
    ws.url(s"$apiUrl/$userId")
      .get()
      .map(_.json.validate[ApiUser])
      .map(_.asOpt)
}
