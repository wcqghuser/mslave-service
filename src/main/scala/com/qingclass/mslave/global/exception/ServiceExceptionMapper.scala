package com.qingclass.mslave.global.exception

import com.google.inject.{Inject, Singleton}
import com.qingclass.mslave.global.config.ServerConfig
import com.qingclass.mslave.global.controller.common.ApiResponse
import com.qingclass.mslave.global.util.SysLogging
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.exceptions.ExceptionMapper
import com.twitter.finatra.http.response.ResponseBuilder

@Singleton
class ServiceExceptionMapper @Inject()(response: ResponseBuilder)
  extends ExceptionMapper[ServiceException] with SysLogging {

  override def toResponse(request: Request, exception: ServiceException): Response = {
    error("ServiceException", exception.getMessage)
    response.internalServerError(ApiResponse.http5xx(ServerConfig.SERVICE_NAME.concat(s"(${exception.getMessage})")))
  }
}

class ServiceException(msg: String = "service exception") extends Exception(msg)