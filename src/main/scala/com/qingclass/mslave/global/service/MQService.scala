package com.qingclass.mslave.global.service

import java.util

import com.qingclass.mslave.global.config.EnvironmentConfig
import com.qingclass.mslave.global.util.ConnectionPool
import com.rabbitmq.client.Connection

object MQService {

  val host = EnvironmentConfig.getString("mq.host")
  val username = EnvironmentConfig.getString("mq.username")
  val password = EnvironmentConfig.getString("mq.password")
  val virtualHost = EnvironmentConfig.getString("mq.virtualHost")
  val xHaPolicy = EnvironmentConfig.getString("mq.x-ha-policy")
  val connectionPool = new ConnectionPool(host, username, password, virtualHost)

  def getConnection[T](body: Connection => T) = connectionPool.getConnection(body)

  def getXHaPolicyConf = {
    if (xHaPolicy.nonEmpty) {
      val map = new util.HashMap[String, Object]()
      map.put("x-ha-policy", "all")
      map
    } else null
  }
}
