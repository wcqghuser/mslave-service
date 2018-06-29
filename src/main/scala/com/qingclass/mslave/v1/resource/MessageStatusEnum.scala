package com.qingclass.mslave.v1.resource

/**
  * wechat message status
  * INIT
  * PROCESSING
  * SCHEDULED
  * SENDING
  * SUCCESS
  * PARTIAL_SUCCESS
  * FAILED
  * CANCELED
  * TIMEOUT
  *
  * @author wangchaoqun
  */
object MessageStatusEnum extends Enumeration {
  type MessageStatusEnum = Value

  val INIT, SENDING, SUCCESS, PARTIAL_SUCCESS, FAILED, CANCELED, TIMEOUT = Value
}
