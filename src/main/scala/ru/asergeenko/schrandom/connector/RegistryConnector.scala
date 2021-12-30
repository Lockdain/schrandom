package ru.asergeenko.schrandom.connector

import monix.execution.CancelableFuture
import org.apache.avro.Schema

trait RegistryConnector {
  def getSchema(name: String, version: String): CancelableFuture[Schema]
}
