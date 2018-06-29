package com.qingclass.mslave.global.exception

import com.google.inject.{Inject, Singleton}
import com.qingclass.mslave.global.config.ServerConfig
import com.qingclass.mslave.global.controller.common.ApiResponse
import com.qingclass.mslave.global.util.SysLogging
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.exceptions.ExceptionMapper
import com.twitter.finatra.http.response.ResponseBuilder

@Singleton
class Http4xxExceptionMapper @Inject()(response: ResponseBuilder)
  extends ExceptionMapper[Http4xxException] with SysLogging {

  override def toResponse(request: Request, exception: Http4xxException): Response = {
    error("Http4xxException", exception.getMessage)
    response.badRequest(ApiResponse.http4xx(ServerConfig.SERVICE_NAME.concat(s"(${exception.getMessage})")))
  }
}

class Http4xxException(msg: String) extends Exception(msg)
