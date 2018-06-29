package com.qingclass.mslave.global

import com.qingclass.mslave.global.config.{EnvironmentConfig, ServerConfig}
import com.qingclass.mslave.global.controller.InternalController
import com.qingclass.mslave.global.exception.{Http4xxExceptionMapper, MalformedAPIExceptionMapper, ServiceExceptionMapper}
import com.qingclass.mslave.v1.work.MessageSendWork
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter

class Server extends HttpServer {

  private val environment = System.getenv("RUNNING_MODE") match {
    case EnvironmentConfig.PRODUCTION => EnvironmentConfig.PRODUCTION
    case _ => EnvironmentConfig.DEVELOPMENT
  }

  EnvironmentConfig.setEnvironment(environment)

  System.setProperty("scala.concurrent.context.minThreads", 2048.toString)
  System.setProperty("scala.concurrent.context.maxThreads", 4096.toString)

  override protected def defaultFinatraHttpPort: String = ":" + ServerConfig.SERVER_PORT
  override def defaultHttpPort: Int = ServerConfig.SERVER_PROT_ADMIN
  override protected def disableAdminHttpServer: Boolean = true

  override protected def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[InternalController]
      .exceptionMapper[MalformedAPIExceptionMapper]
      .exceptionMapper[Http4xxExceptionMapper]
      .exceptionMapper[ServiceExceptionMapper]
  }

  MessageSendWork.subscribeMessage()
}

object MainServer extends Server
