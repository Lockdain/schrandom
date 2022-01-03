package ru.asergeenko.schrandom.adt

import monix.execution.CancelableFuture
import org.apache.avro.Schema

import scala.concurrent.duration.TimeUnit

case class GeneratorBehavior(
  schema: CancelableFuture[Schema],
  genType: GeneratorType,
  messageType: MessageType)

case class MessageCreationPolicy(period: Long, unit: TimeUnit, qty: Long)
