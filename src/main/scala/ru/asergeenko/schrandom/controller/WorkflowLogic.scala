package ru.asergeenko.schrandom.controller

import monix.eval.Task
import monix.execution.Cancelable
import ru.asergeenko.schrandom.intf.MessageGeneratorBuilder
import ru.asergeenko.schrandom.connector.ApicurioConnector
import ru.asergeenko.schrandom.settings.{ GeneratorBehavior, GeneratorType, MessageType, PublisherSettings }
import monix.execution.Scheduler.{ global => scheduler }
import monix.execution.Scheduler.Implicits.global
import org.apache.avro.Schema
import org.slf4j.LoggerFactory

object WorkflowLogic {
  private val logger = LoggerFactory.getLogger(this.getClass.toString)

  def initiateRegistryWorkflow(settings: PublisherSettings): Cancelable = {
    logger.info(s"Registry workflow for topic=${settings.topic} was initiated")
    val eventualSchema = ApicurioConnector.getSchema(settings.schema.getOrElse(""), settings.version)
    val behavior       = GeneratorBehavior(eventualSchema, GeneratorType.UNBOUNDED, MessageType.JSON)
    val jsonGenerator  = MessageGeneratorBuilder.build(settings.topic, behavior)

    import java.util.concurrent.TimeUnit
    val cancelable: Cancelable = scheduler
      .scheduleAtFixedRate(0, settings.period, TimeUnit.MILLISECONDS, jsonGenerator)

    cancelable
  }

  def initiateStandaloneWorkflow(settings: PublisherSettings, schemaBody: String): Cancelable = {
    logger.info(s"Registry workflow for topic=${settings.topic} was initiated")
    // TODO: That's looks strange
    val schemaTask = Task { Schema.parse(schemaBody) }
    val eventualSchema = schemaTask.runToFuture
    val behavior   = GeneratorBehavior(eventualSchema, GeneratorType.UNBOUNDED, MessageType.JSON)
    val jsonGenerator  = MessageGeneratorBuilder.build(settings.topic, behavior)

    import java.util.concurrent.TimeUnit
    val cancelable: Cancelable = scheduler
      .scheduleAtFixedRate(0, settings.period, TimeUnit.MILLISECONDS, jsonGenerator)

    cancelable
  }
}
