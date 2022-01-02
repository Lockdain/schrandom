package ru.asergeenko.schrandom.connector

import monix.execution.CancelableFuture
import org.apache.kafka.clients.producer.KafkaProducer

trait KafkaConnector {

  def createJsonProducer(bootstrapServers: String): KafkaProducer[String, String]

  def publishAvro()

  def publishJson(topic: String, event: CancelableFuture[String], producer: KafkaProducer[String, String]): Unit

}
