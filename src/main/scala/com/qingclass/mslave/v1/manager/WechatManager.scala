package com.qingclass.mslave.v1.manager

import com.qingclass.mslave.global.util.RestClient
import com.qingclass.mslave.v1.cache.TokenCache
import com.qingclass.mslave.v1.resource.MessageTypeEnum

object WechatManager {

  def sendMessaage(openId: String, msg: String, appId: String, msgType: Int, retry: Int = 0): (String, String) = {
    try {
      val token = TokenCache.getAccessToken(appId).token
      val url = if (msgType.equals(MessageTypeEnum.TEMPLATE.id)) templateUrl + token else mpnewsUrl + token
      val body = msg.replaceAll(replaceStr, openId)
      val header = Some(Seq(("Content-Type", "application/json;charset=UTF-8")))
      val response = RestClient.post(url, body, header)
      val code = response.getOrElse("errcode", CodeObject.DEFAULT_CODE)
      val errmsg = response.getOrElse("errmsg", "")
      if (
        CodeObject.SUCCESS.equals(code) ||
        CodeObject.REQUIRE_SUBSCRIBE.equals(code) ||
        CodeObject.RESPONSE_TIME_OUT.equals(code) ||
        CodeObject.TOKEN_EXPIRED.equals(code)
      ) {
        (code, errmsg)
      } else {
        if (retry < 3) sendMessaage(openId, msg, appId, msgType, retry + 1)
        else (code, errmsg)
      }
    } catch {
      case e: Exception =>
        if (retry < 3) sendMessaage(openId, msg, appId, msgType, retry + 1) else (CodeObject.DEFAULT_CODE, e.toString)
    }
  }

  val templateUrl: String = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
  val mpnewsUrl: String = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
  val replaceStr = "OPENID"
}

object CodeObject {
  val SUCCESS = "0"
  val TOKEN_EXPIRED = "42001"
  val REQUIRE_SUBSCRIBE = "43004"
  val RESPONSE_TIME_OUT = "45015"
  val DEFAULT_CODE = "-1"
}