package com.qingclass.mslave.global.config

import scalacache.ScalaCache
import scalacache.guava.GuavaCache

trait QingClassCache {
  implicit val scalaCache = ScalaCache(GuavaCache())
}
