package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.ws.WSClient
import play.api.test.Helpers._

class CommentsControllerSpec extends PlaySpec with GuiceOneServerPerSuite {

  "CommentsController.comments" should {
    "deliver important content" in {
      val wsClient = app.injector.instanceOf[WSClient]
      val commentsUrl = s"http://localhost:$port/comments"
      val response = await(wsClient.url(s"$commentsUrl/1").get())

      response.status mustBe OK
      response.contentType mustBe "text/html; charset=UTF-8"
      response.body must include("Important Content")
    }
  }
}
