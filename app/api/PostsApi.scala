package api

import javax.inject._
import model.api.ApiPost
import play.api.Configuration
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PostsApi @Inject()(config: Configuration, ws: WSClient)(
    implicit ex: ExecutionContext) {

  implicit val postReads = Json.reads[ApiPost]
  val apiUrl = config.get[String]("postsApiBaseUrl")

  def retrievePosts(userId: Int): Future[Option[Seq[ApiPost]]] =
    ws.url(s"$apiUrl?userId=$userId")
      .get()
      .map(_.json.validate[Seq[ApiPost]])
      .map(_.asOpt)
}
