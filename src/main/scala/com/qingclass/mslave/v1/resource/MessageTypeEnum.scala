package com.qingclass.mslave.v1.resource

/**
  * wechat template message: template、mapnews
  * example{{{
  *   template:
  *   {
  *         "touser":"OPENID",
  *         "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
  *         "url":"http://weixin.qq.com/download",
  *         "miniprogram":{
  *           "appid":"xiaochengxuappid12345",
  *           "pagepath":"index?foo=bar"
  *         },
  *         "data":{
  *                 "first": {
  *                     "value":"恭喜你购买成功！",
  *                     "color":"#173177"
  *                 },
  *                 "keynote1":{
  *                     "value":"巧克力",
  *                     "color":"#173177"
  *                 },
  *                 "keynote2": {
  *                     "value":"39.8元",
  *                     "color":"#173177"
  *                 },
  *                 "keynote3": {
  *                     "value":"2014年9月22日",
  *                     "color":"#173177"
  *                 },
  *                 "remark":{
  *                     "value":"欢迎再次购买！",
  *                     "color":"#173177"
  *                 }
  *         }
  *     }
  *
  *  mapnews:
  *  {
  *    "touser":"OPENID",
  *    "msgtype":"news",
  *    "news":{
  *        "articles": [
  *         {
  *             "title":"Happy Day",
  *             "description":"Is Really A Happy Day",
  *             "url":"URL",
  *             "picurl":"PIC_URL"
  *         },
  *         {
  *             "title":"Happy Day",
  *             "description":"Is Really A Happy Day",
  *             "url":"URL",
  *             "picurl":"PIC_URL"
  *         }
  *         ]
  *    }
  * }}}
  *
  * @author wangchaoqun
  */
object MessageTypeEnum extends Enumeration {
  type MessageTypeEnum = Value

  val UNKNOWN, TEMPLATE, MAPNEWS = Value
}