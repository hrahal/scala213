package com.bunic.scala213
package util
package http

import akka.http.scaladsl.model.IllegalRequestException
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler => AkkaExceptionHandler}
import com.bunic.scala213.http.Error
import com.typesafe.scalalogging.LazyLogging

trait ExceptionHandler { self: LazyLogging =>
  val exceptionHandler = AkkaExceptionHandler {
    case ire: IllegalRequestException =>
      extractUri { uri =>
        logger.error("Request to {} could not be handled normally", uri)
        complete(Error(BadRequest, ire.getMessage).toHttpResponse)
      }
    case iae: IllegalArgumentException =>
      extractUri { uri =>
        logger.error("Request to {} could not be handled normally", uri)
        complete(Error(BadRequest, iae.getMessage).toHttpResponse)
      }
    case _: ArithmeticException =>
      extractUri { uri =>
        logger.error("Request to {} could not be handled normally", uri)
        complete(Error(InternalServerError, "Arithmetic Exception Occured").toHttpResponse)
      }
    case e: Exception =>
      extractUri { uri =>
        logger.error("Request to {} could not be handled normally", uri)
        logger.error("Exception during client request processing: {}", e.getMessage)
        complete(Error(InternalServerError, "Internal Server Error Occured").toHttpResponse)
      }
  }
}
