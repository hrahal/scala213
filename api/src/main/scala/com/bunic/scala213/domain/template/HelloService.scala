package com.bunic.scala213.domain.template

import akka.http.scaladsl.model.StatusCodes
import com.bunic.scala213.http.Error
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

/** Service
  * Interacts with twitter api
  *
  * @param repo TweetRepository provides communication with Twitter
  */
class HelloService(repo: HelloRepositoryAlgebra)(implicit ec: ExecutionContext) extends LazyLogging {

  import cats.data._
  import cats.implicits._

  type Response[Success] = EitherT[Future, Error, Success]
  type TransformedTweet = String

  def tweets(username: String, limit: Int): Response[Seq[TransformedTweet]] = EitherT {
    repo
      .searchByUserName(username, limit)
      .map(_.map(_.transform).asRight[Error])
      .recover {
        case e: Throwable =>
          logger.error("Error occurred during loading tweets", e)
          Error(StatusCodes.InternalServerError, "Twitter api is not available").asLeft
      }
  }

}
