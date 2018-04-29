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
      .configure(
        "userApiBaseUrl" -> s"http://localhost:$port/mocked-api/users",
        "postsApiBaseUrl" -> s"http://localhost:$port/mocked-api/posts")
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
        case GET(p"/mocked-api/users/3") =>
          Action {
            InternalServerError
          }
        case GET(p"/mocked-api/posts" ? q"userId=$userId") =>
          Action {
            if (Seq("1", "2", "3").contains(userId))
              Ok("""[{"title":"First Comment Title", "body":"First Comment Body"},
                  |{"title":"Second Comment Title", "body":"Second Comment Body"}]""".stripMargin)
            else
              InternalServerError
          }
      })
      .build()
  }

  "CommentsController.comments" should {
    "deliver commentator's username and comments and the important content when username is given" in {
      val response = requestFor(1)

      response.body must include("Alice")
      response.body must include("First Comment Title")
      response.body must include("First Comment Body")
      response.body must include("Second Comment Title")
      response.body must include("Second Comment Body")
      response.body must include("Important Content")
    }

    "deliver the commentator's comments and the important content when username is not given" in {
      val response = requestFor(2)

      response.body must include("First Comment Title")
      response.body must include("First Comment Body")
      response.body must include("Second Comment Title")
      response.body must include("Second Comment Body")
      response.body must include("Important Content")
    }

    "deliver the commentator's comments and important content when user api is broken" in {
      val response = requestFor(3)

      response.body must include("First Comment Title")
      response.body must include("First Comment Body")
      response.body must include("Second Comment Title")
      response.body must include("Second Comment Body")
      response.body must include("Important Content")
    }
  }

  private def requestFor(userId: Int): WSResponse = {
    val wsClient = app.injector.instanceOf[WSClient]
    val commentsUrl = s"http://localhost:$port/comments/$userId"
    val response = await(wsClient.url(commentsUrl).get())

    response.status mustBe OK
    response.contentType mustBe "text/html; charset=UTF-8"

    response
  }

}
