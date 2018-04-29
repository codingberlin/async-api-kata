package controllers

import api.UserApi
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class CommentsController @Inject()(cc: ControllerComponents, userApi: UserApi)(
    implicit ex: ExecutionContext)
    extends AbstractController(cc) {

  def comments(userId: Int) = Action.async {
    implicit request: Request[AnyContent] =>
      userApi
        .retrieveUser(userId)
        .map(views.html.comments(_))
        .map(Ok(_))
  }
}
