package ru.asergeenko.schrandom.generator

import org.apache.avro.Schema
import org.apache.avro.util.RandomData
import scala.jdk.CollectionConverters.IteratorHasAsScala

class EventFlooder(schema: Schema) {
  def createSingleJsonEvent: String = {
    val event = new RandomData(schema, 1)
    event.iterator().asScala.toList.head.toString
  }

  def createMultipleJsonEvents(qty: Int) = {
    val events = new RandomData(schema, qty)
    events.iterator().asScala.toList.map(_.toString)
  }
}
