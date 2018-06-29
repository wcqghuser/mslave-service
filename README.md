# MessageSlave服务

## 技术选型
- scala
- finatra
- akka-stream
- redis
- rabbitmq

## 主要功能
mmaster发布要推送队列的redis key
slave订阅接收到key开始pop redis队列内的元素
获取到的每个元素作为source放入stream消费
Source.single(msg) -> sink.foreach(_send)
