package services

import api.{PostsApi, UserApi}
import javax.inject.{Inject, Singleton}
import model.api.ApiPost
import model.frontend.Comment
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
class CommentsService @Inject()(userApi: UserApi, postsApi: PostsApi)(
    implicit ex: ExecutionContext) {

  def retrieveComments(userId: Int): Future[Seq[Comment]] = {
    val userNameFuture = retrieveUserName(userId)
    val postsFuture = retrievePosts(userId)

    for {
      username <- userNameFuture
      posts <- postsFuture
    } yield mergeToFrontendComments(username, posts)
  }

  private def retrieveUserName(userId: Int): Future[Option[String]] =
    userApi
      .retrieveUser(userId)
      .map(_.map(_.username))
      .recover {
        case NonFatal(throwable) =>
          Logger.error("Could not retrieve user from api", throwable)
          None
      }

  private def retrievePosts(userId: Int): Future[Seq[ApiPost]] =
    postsApi
      .retrievePosts(userId)
      .map(_.getOrElse(Seq.empty))
      .recover {
        case NonFatal(throwable) =>
          Logger.error("Could not retrieve posts from api", throwable)
          Seq.empty
      }

  private def mergeToFrontendComments(userName: Option[String],
                                      posts: Seq[ApiPost]): Seq[Comment] =
    posts.map(post =>
      Comment(name = userName, title = post.title, body = post.body))

}
