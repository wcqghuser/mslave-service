package com.qingclass.mslave.global.util

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

object TimeUtil {

  def getCurrentDateTime: String = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime)

  def getCurrentDay: String = new SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance().getTime)

  def parseToDate(dateStr: String) = new SimpleDateFormat(DATE_FORMAT).parse(dateStr)

  def parseToDateFull(dateStr: String) = new SimpleDateFormat(DATE_FULL_FORMAT).parse(dateStr)

  def parseToString(date: Date) = new SimpleDateFormat(DATE_FORMAT).format(date)

  def parseToStringLocal(date: Date) = new SimpleDateFormat(DATE_FORMAT_LOCAL).format(date)

  def parseToStringFull(date: Date) = new SimpleDateFormat(DATE_FULL_FORMAT).format(date)

  def parseToStringFullLocal(date: Date) = new SimpleDateFormat(DATE_FULL_LOCAL_FORMAT).format(date)

  def parseToStringFull(dateMil: Long) = new SimpleDateFormat(DATE_FULL_FORMAT).format(new Date(dateMil))

  def parseToStringFullLocal(dateMil: Long) = new SimpleDateFormat(DATE_FULL_LOCAL_FORMAT).format(new Date(dateMil))

  def getYesterday: String = getSpecifiedDay(-1)

  def getSpecifiedDay(n: Int) = {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, n)
    new SimpleDateFormat(DATE_FORMAT).format(calendar.getTime)
  }

  val DATE_FORMAT: String = "yyyy-MM-dd"
  val DATE_FORMAT_LOCAL: String = "yyyy年MM月dd日"
  val DATE_FULL_FORMAT: String = "yyyy-MM-dd HH:mm:ss"
  val DATE_FULL_LOCAL_FORMAT: String = "yyyy年MM月dd日 HH:mm:ss"
}
