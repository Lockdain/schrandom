package ru.asergeenko.schrandom.intf.connector

trait KafkaConnector {
  def publishAvro

  def publishJson(topic: String, event: String)

}
