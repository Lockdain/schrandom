package ru.asergeenko.schrandom.settings

import org.apache.avro.Schema

import scala.concurrent.duration.TimeUnit

case class GeneratorBehavior(
  schema: Schema,
  policy: MessageCreationPolicy,
  genType: GeneratorType,
  messageType: MessageType)

case class MessageCreationPolicy(period: Long, unit: TimeUnit, qty: Long)
