package com.qingclass.mslave.global.controller.common

import com.qingclass.mslave.global.resource.StatusCodeEnum
import com.qingclass.mslave.global.service.Common.BaseDTO
import com.qingclass.mslave.global.util.SysLogging

object ApiResponse extends SysLogging {
  def success = BaseDTO(StatusCodeEnum.SUCCESS.id, StatusCodeEnum.SUCCESS.toString)

  def success(data: Any) = BaseDTO(StatusCodeEnum.SUCCESS.id, StatusCodeEnum.SUCCESS.toString, data)

  def success(map: Map[String, Any]) = BaseDTO(StatusCodeEnum.SUCCESS.id, StatusCodeEnum.SUCCESS.toString, map)

  def http4xx(error: String) = BaseDTO(StatusCodeEnum.HTTP_4XX.id, StatusCodeEnum.HTTP_4XX.toString, error)

  def http5xx(error: String) = BaseDTO(StatusCodeEnum.HTTP_5XX.id, StatusCodeEnum.HTTP_5XX.toString, error)
}
