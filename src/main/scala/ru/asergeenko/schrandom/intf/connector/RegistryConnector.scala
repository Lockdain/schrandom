package ru.asergeenko.schrandom.intf.connector

import monix.execution.CancelableFuture
import org.apache.avro.Schema

trait RegistryConnector {
  def getSchema(name: String, version: String): CancelableFuture[Schema]
}
