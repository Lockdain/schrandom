package ru.asergeenko.schrandom.connector

import monix.execution.CancelableFuture

trait KafkaConnector {
  def publishAvro

  def publishJson(topic: String, event: CancelableFuture[String]): Unit

}
