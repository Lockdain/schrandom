package ru.asergeenko.schrandom.generator

import monix.execution.CancelableFuture
import org.apache.avro.Schema
import org.apache.avro.util.RandomData
import monix.execution.Scheduler.Implicits.global

import scala.jdk.CollectionConverters.IteratorHasAsScala

class EventFlooder(eventualSchema: CancelableFuture[Schema]) {
  def createSingleJsonEvent: CancelableFuture[String] = {
    eventualSchema.map(schema =>
      new RandomData(schema, 1)
        .iterator()
        .asScala
        .toList
        .head
        .toString
    )
  }
}
