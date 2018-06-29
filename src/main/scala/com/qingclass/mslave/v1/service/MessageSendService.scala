package com.qingclass.mslave.v1.service

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, SourceShape}
import akka.stream.scaladsl.{Flow, GraphDSL, Sink, Source}
import com.qingclass.mslave.global.config.{CacheName, EnvironmentConfig, QingClassCache, RedisConfig}
import com.qingclass.mslave.global.db.RedisCache
import com.qingclass.mslave.global.util.JsonUtil._
import com.qingclass.mslave.global.util.{CommonUtil, SysLogging, TimeUtil}
import com.qingclass.mslave.v1.manager.{CodeObject, WechatManager}

import scalacache._

object MessageSendService extends QingClassCache with SysLogging {

  implicit val sys = ActorSystem("streamSys")
  implicit val ec = sys.dispatcher
  implicit val mat = ActorMaterializer()

  def sendMessage(key: String) = {
    var currentPoolSize: Long = RedisCache.llen(key).getOrElse(0L)
    var count = RedisCache.llen(key).getOrElse(0L)
    while (count > 0) {
      currentPoolSize = sync.get(CacheName.POOL_THREAD_COUNT_CACHE).getOrElse(0L)
      if (currentPoolSize < maxPoolSize) {
        val msg = RedisCache.lpop(key).getOrElse("")
        dealWith(key, msg)
        val size = sync.get(CacheName.POOL_THREAD_COUNT_CACHE).getOrElse(0L)
        put(CacheName.POOL_THREAD_COUNT_CACHE)(size - 1)
      }
      count = RedisCache.llen(key).getOrElse(0L)
    }
  }

  private def dealWith(key: String, msg: String) = {
    def _send(msg: String) = {
      val map = msg.toJsonMap[Any]
      val messageType = map("messageType").toString.toInt
      val message = map("message").toString
      val openId = map("openId").toString
      val appId = map("appId").toString
      debug(s"openId=$openId & appId=$appId & message=$message & messageType=$messageType")
      //Determine whether course push
      if (map.contains("courseNumber")) {
        val courseNumber = CommonUtil.anyToInt(map("courseNumber"))
        val currentDay = TimeUtil.getCurrentDay
        val courseKey = RedisConfig.COMPLETE_KEY.format(currentDay)
        val studyCourseNum = CommonUtil.anyToInt(RedisCache.hget(courseKey, openId).getOrElse(0))
        //Have learned all courses do not push the msg
        if (courseNumber > studyCourseNum) {
          val newMessage = message.replaceAll(study_num, studyCourseNum.toString).replaceAll(course_num, courseNumber.toString)
          val (code, errMsg) = WechatManager.sendMessaage(openId, newMessage, appId, messageType)
          if (!code.equals(CodeObject.SUCCESS)) {
            if (code.equals(CodeObject.TOKEN_EXPIRED)) RedisCache.rpush(key, msg)
            error(s"send message fail openId=$openId & appId=$appId & code=$code & errMsg=$errMsg & message=$newMessage")
          }
        }
      } else {
        val (code, errMsg) = WechatManager.sendMessaage(openId, message, appId, messageType)
        if (!code.equals(CodeObject.SUCCESS)) {
          if (code.equals(CodeObject.TOKEN_EXPIRED)) RedisCache.rpush(key, msg)
          error(s"send message fail openId=$openId & appId=$appId & code=$code & errMsg=$errMsg & message=$message")
        }
      }
    }

    val source = Source.single(msg)
    val sink = Sink.foreach(_send)

    val sourceGraph = GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._
      val src = source.filter(_.nonEmpty)
      val pipe = builder.add(Flow[String])
      src ~> pipe.in
      SourceShape(pipe.out)
    }

    Source.fromGraph(sourceGraph).runWith(sink)
  }

  private val study_num = "#STUDYNUM#"
  private val course_num = "#COURSENUM#"
  private val maxPoolSize = EnvironmentConfig.getInt("calculate_thread_count")
}