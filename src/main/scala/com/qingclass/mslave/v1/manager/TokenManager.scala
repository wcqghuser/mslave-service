package com.qingclass.mslave.v1.manager

import com.qingclass.mslave.global.config.EnvironmentConfig
import com.qingclass.mslave.global.util.{CommonUtil, RestClient}
import com.qingclass.mslave.v1.domain.AccessToken

object TokenManager {

  def getAccessToken(appId: String) = {
    val url = tokenServerBaseURL.replaceAll(replaceStr, appId)
    val response = RestClient.getAny(url)
    val data = CommonUtil.transData2Json(response)._2.asInstanceOf[Map[String, Any]]
    val map = data.getOrElse("access_token", Map()).asInstanceOf[Map[String, Any]]
    AccessToken(
      map.getOrElse("token", "").toString,
      CommonUtil.anyToLong(map.getOrElse("ttl", 0))
    )
  }

  val tokenServerBaseURL = EnvironmentConfig.getString("access_token")
  val replaceStr = "APPID"
}
