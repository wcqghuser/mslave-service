package com.qingclass.mslave.global.domain

case class HealthResult(name: String, status: String, threads: Int, message: String = "")
