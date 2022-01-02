package ru.asergeenko.schrandom.connector

import monix.execution.CancelableFuture
import org.apache.avro.Schema
import ru.asergeenko.schrandom.conf.AnyHostPort

trait RegistryConnector {
  def getSchema(name: String, version: String, hostPort: AnyHostPort): CancelableFuture[Schema]
}
