package ru.asergeenko.schrandom.controller

import org.slf4j.LoggerFactory
import pureconfig.ConfigReader.Result
import pureconfig.ConfigSource
import ru.asergeenko.schrandom.conf.ServiceProps
import pureconfig.generic.auto._
import sttp.tapir.server.netty.NettyFutureServer
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SwaggerController {
  private val config: Result[ServiceProps] = ConfigSource.default.load[ServiceProps]
  private val logger                       = LoggerFactory.getLogger(this.getClass.toString)
  private val port                                 = config.map(_.swagger.port).toOption.get
  private val swaggerEndpoints                     =
    SwaggerInterpreter().fromServerEndpoints[Future](SchrandomController.endpoints, "Schrandom", "0.0.1")

  NettyFutureServer()
    .addEndpoints(swaggerEndpoints)
    .host("0.0.0.0")
    .port(port.toInt)
    .start()

  logger.info(s"Swagger UI started at http://localhost:$port/docs.")
}
