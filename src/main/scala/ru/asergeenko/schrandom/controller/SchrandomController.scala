package ru.asergeenko.schrandom.controller

import monix.execution.Cancelable
import sttp.tapir._
import sttp.tapir.server.netty.NettyFutureServer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SchrandomController {
  var schedulers = scala.collection.mutable.Map[String, Cancelable]()

  val controlEndpoint = endpoint.get
    .in("schrandom")
    .in(query[String]("topic"))
    .in(query[String]("schema"))
    .in(query[String]("version"))
    .out(stringBody)
    .serverLogic { case (topic, schema, version) =>
      schedulers(topic) = WorkflowLogic.initiateWorkflow(topic, schema, version)
      Future.successful[Either[Unit, String]](
        Right(s"Request received with topic=$topic, schema=$schema and version=$version")
      )
    }

  val binding = NettyFutureServer()
    .addEndpoint(controlEndpoint)
    .host("0.0.0.0")
    .port(8085)
    .start()
}
