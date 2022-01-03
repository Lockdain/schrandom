package ru.asergeenko.schrandom.generator

import monix.execution.CancelableFuture
import org.apache.avro.Schema
import org.apache.avro.util.RandomData
import monix.execution.Scheduler.Implicits.global
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters.IteratorHasAsScala

class EventFlooder(eventualSchema: CancelableFuture[Schema]) {
  private val logger = LoggerFactory.getLogger(this.getClass.toString)
  
  def createSingleJsonEvent: CancelableFuture[String] = {
    eventualSchema.map { schema =>
      val randomEvent = new RandomData(schema, 1)
        .iterator()
        .asScala
        .toList
        .head
        .toString
      logger.trace(s"Random event generated: $randomEvent")
      randomEvent
    }
  }

  def createMultipleJsonEvents(messageQty: Int): CancelableFuture[List[String]] = {
    eventualSchema.map { schema =>
      val randomEvents = new RandomData(schema, messageQty)
        .iterator()
        .asScala
        .toList
        .map(_.toString)
      logger.trace(s"Random events of size ${randomEvents.size} generated.")
      randomEvents
    }
  }
}
