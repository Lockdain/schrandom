package ru.asergeenko.schrandom.settings

import org.apache.avro.Schema

import scala.concurrent.duration.TimeUnit

case class GeneratorBehavior(schema: Schema, policy: MessagePolicy, genType: GeneratorType)

case class MessagePolicy(period: Long, unit: TimeUnit, qty: Long)
