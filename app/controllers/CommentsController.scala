package controllers

import javax.inject._
import play.api.mvc._
import services.CommentsService

import scala.concurrent.ExecutionContext

@Singleton
class CommentsController @Inject()(
    cc: ControllerComponents,
    commentsService: CommentsService)(implicit ex: ExecutionContext)
    extends AbstractController(cc) {

  def comments(userId: Int) = Action.async {
    implicit request: Request[AnyContent] =>
      commentsService
        .retrieveComments(userId)
        .map(views.html.comments(_))
        .map(Ok(_))
  }
}
