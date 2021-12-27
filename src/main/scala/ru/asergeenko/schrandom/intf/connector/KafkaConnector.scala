package ru.asergeenko.schrandom.intf.connector

import monix.execution.CancelableFuture

trait KafkaConnector {
  def publishAvro

  def publishJson(topic: String, event: CancelableFuture[String]): Unit

}
