package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class CommentsController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc) {

  def comments(userId: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.comments())
  }
}
