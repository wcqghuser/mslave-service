package com.qingclass.mslave.v1.work

import com.qingclass.mslave.global.config.CacheName
import com.qingclass.mslave.global.service.MQService
import com.qingclass.mslave.global.util.SysLogging
import com.qingclass.mslave.v1.service.MessageSendService
import com.rabbitmq.client.{AMQP, DefaultConsumer, Envelope}

object MessageSendWork extends SysLogging {

  def subscribeMessage() = {
    MQService.getConnection{ connection =>
      val channel = connection.createChannel()
      val headers = MQService.getXHaPolicyConf
      channel.exchangeDeclare(CacheName.MESSAGE_MQ_EXCHANGE, "fanout", true, false, headers)
      val queueName = channel.queueDeclare().getQueue
      channel.queueBind(queueName, CacheName.MESSAGE_MQ_EXCHANGE, "")
      val consumer = new DefaultConsumer(channel) {

        override def handleDelivery(consumerTag: String,
                                    envelope: Envelope,
                                    properties: AMQP.BasicProperties,
                                    body: Array[Byte]) {
          try {
            val message = new String(body, "UTF-8")
            MessageSendService.sendMessage(message)
          } catch {
            case e: Exception => error("send message error", e)
          } finally {
            channel.basicAck(envelope.getDeliveryTag, false)
          }
        }
      }
      channel.basicConsume(queueName, false, consumer)
    }
  }

}
