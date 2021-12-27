package ru.asergeenko.schrandom.intf.connector

import pureconfig.ConfigReader.Result
import pureconfig.ConfigSource
import ru.asergeenko.schrandom.conf.ServiceProps
import pureconfig.generic.auto._
import monix.kafka._
import monix.execution.{ CancelableFuture, Scheduler }

class KafkaProducer extends KafkaConnector {
  private val config: Result[ServiceProps]  = ConfigSource.default.load[ServiceProps]
  private val host                          = config.map(_.kafka.host).toOption.get
  private val port                          = config.map(_.kafka.port).toOption.get
  private implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  private val producerConfig = KafkaProducerConfig.default.copy(
    bootstrapServers = List(host + ":" + port)
  )
  private val jsonProducer   = KafkaProducer[String, String](producerConfig, scheduler)

  override def publishAvro: Unit = {}

  override def publishJson(topic: String, event: CancelableFuture[String]): Unit = {
    event
      .onComplete(value =>
        jsonProducer.send(topic, value.get)
      )
  }
}
