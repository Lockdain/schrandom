package ru.asergeenko.schrandom.intf.connector

import org.apache.avro.Schema

import scala.concurrent.Future

trait RegistryConnector {
  def getSchema(name: String, version: String): Future[Schema]
}
