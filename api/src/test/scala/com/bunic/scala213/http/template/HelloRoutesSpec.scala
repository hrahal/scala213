package com.bunic.scala213.http.template

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.MissingQueryParamRejection
import cats.data._
import cats.implicits._
import com.bunic.scala213.domain.template.HelloService
import com.bunic.scala213.UnitSpec
import com.bunic.scala213.http._
import com.bunic.scala213.util.http.{ExceptionHandler, RejectionHandler}
import com.typesafe.scalalogging.LazyLogging
import org.scalamock.scalatest.AsyncMockFactory

import scala.concurrent.Future

class HelloRoutesSpec extends UnitSpec with AsyncMockFactory {

  "The shout route" when {
    "called with 'GET /shout/'" should {
      "return with a HTTP status code of 200 and a JSON 'Seq[String]' response" in {
        val ctx = new Context with StubData
        import ctx._

        val mock: svc.Response[Seq[String]] = EitherT(
          Future(tweets.asRight[Error])
        )
        (svc.tweets _).when("username", 10).returns(mock)

        Get("/tweet/username?limit=10") ~> rte ~> check {
          status.intValue() shouldBe 200
          status shouldEqual StatusCodes.OK
        }
      }

      "reject with a MissingQueryParamRejection for wrong field name" in {
        val ctx = new Context with StubData
        import ctx._

        Get("/tweet/username?limi=10") ~> rte ~> check {
          rejection shouldBe an[MissingQueryParamRejection]
        }
      }

      "return with a HTTP status code of 400 and a JSON 'Error' response" in {
        val ctx = new Context with StubData
        import ctx._

        val mock: svc.Response[Seq[String]] = EitherT(
          Future(
            Error(StatusCodes.InternalServerError, "Error occurred during getting tweets").asLeft[Seq[String]]
          )
        )
        (svc.tweets _).when("username", 10).returns(mock)

        Get("/tweet/username?limit=10") ~> rte ~> check {
          status shouldEqual StatusCodes.InternalServerError
          contentType shouldEqual ContentTypes.`application/json`
        }
      }
    }
  }

  trait Context extends LazyLogging with ExceptionHandler with RejectionHandler {

    val actsys: ActorSystem = system

    val svc: HelloService = stub[HelloService]
    val rte = handleExceptions(exceptionHandler) {
      new template.HelloRoutes(svc).route
    }
  }

}
