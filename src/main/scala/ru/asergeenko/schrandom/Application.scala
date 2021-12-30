package ru.asergeenko.schrandom

import org.slf4j.LoggerFactory
import pureconfig.ConfigReader.Result
import pureconfig.ConfigSource
import ru.asergeenko.schrandom.conf.ServiceProps
import pureconfig.generic.auto._
import ru.asergeenko.schrandom.controller.SchrandomController

import scala.io.Source

object Application extends App {
  private val logger = LoggerFactory.getLogger(this.getClass.toString)
  private val asciiLogo = Source.fromResource("logo.txt").getLines.mkString("\n")
  private val schrandomController: SchrandomController.type = SchrandomController
  private val config: Result[ServiceProps] = ConfigSource.default.load[ServiceProps]

  logger.debug(s"Service configs: \n $config")
  logger.info("\n" + asciiLogo)
}
