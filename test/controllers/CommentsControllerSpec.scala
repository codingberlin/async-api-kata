package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.Action
import play.api.mvc.Results._
import play.api.routing.Router
import play.api.test.Helpers._

class CommentsControllerSpec extends PlaySpec with GuiceOneServerPerSuite {

  override def fakeApplication(): Application = {
    import play.api.routing.sird._

    new GuiceApplicationBuilder()
      .configure("userApiBaseUrl" -> s"http://localhost:$port/mocked-api/users")
      // deprication warning can't be resolved while issue is open: https://github.com/playframework/playframework/issues/7553
      .additionalRouter(Router.from {
        case GET(p"/mocked-api/users/1") =>
          Action {
            Ok("""{"username":"Alice"}""")
          }
        case GET(p"/mocked-api/users/2") =>
          Action {
            Ok("""{}""")
          }
      })
      .build()
  }

  "CommentsController.comments" should {
    "deliver commentator's username and the important content when username is given" in {
      val response = requestFor(1)

      response.status mustBe OK
      response.contentType mustBe "text/html; charset=UTF-8"
      response.body must include("Alice")
      response.body must include("Important Content")
    }

    "deliver the important content when username is not given" in {
      val response = requestFor(2)

      response.status mustBe OK
      response.contentType mustBe "text/html; charset=UTF-8"
      response.body must include("Important Content")
    }
  }

  private def requestFor(userId: Int): WSResponse = {
    val wsClient = app.injector.instanceOf[WSClient]
    val commentsUrl = s"http://localhost:$port/comments/$userId"

    await(wsClient.url(commentsUrl).get())
  }

}
