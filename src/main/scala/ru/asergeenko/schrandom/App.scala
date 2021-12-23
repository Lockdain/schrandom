package ru.asergeenko.schrandom

import akka.actor.ActorSystem
import org.slf4j.LoggerFactory
import ru.asergeenko.schrandom.intf.connector.ApicurioConnector

import scala.util.{Failure, Success}

object App extends App {
  private implicit val system = ActorSystem("app")
  private implicit val executionContext = system.dispatcher
  private val logger = LoggerFactory.getLogger(this.getClass.toString)

  private val eventualSchema = ApicurioConnector.getSchema("trigger-value", "1")
  eventualSchema
    .onComplete {
      case Success(value) => logger.info(s"Nice $value")
      case Failure(exception) => logger.error("Unable to get schema", exception)
    }
}
