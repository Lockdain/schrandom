package ru.asergeenko.schrandom.controller

import monix.execution.Cancelable
import sttp.tapir._
import sttp.tapir.server.netty.NettyFutureServer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SchrandomController {
  private var schedulers = scala.collection.mutable.Map[String, Cancelable]()

  private val engageEndpoint = endpoint.get
    .in("schrandom" / "engage")
    .in(query[String]("topic"))
    .in(query[String]("schema"))
    .in(query[String]("version"))
    .out(stringBody)
    .serverLogic { case (topic, schema, version) =>
      schedulers(topic) = WorkflowLogic.initiateWorkflow(topic, schema, version)
      Future.successful[Either[Unit, String]](
        Right(s"Request received with topic=$topic, schema=$schema and version=$version.")
      )
    }

  private val disengageEndpoint = endpoint.get
    .in("schrandom" / "disengage")
    .in(query[String]("topic"))
    .out(stringBody)
    .serverLogic { topic =>
      schedulers.get(topic).foreach(_.cancel)
      Future.successful[Either[Unit, String]](
        Right(s"Disengagement request for topic $topic is received.")
      )
    }

  NettyFutureServer()
    .addEndpoint(engageEndpoint)
    .addEndpoint(disengageEndpoint)
    .host("0.0.0.0")
    .port(8085)
    .start()
}
