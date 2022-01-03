package ru.asergeenko.schrandom

import pureconfig.ConfigReader.Result
import pureconfig.ConfigSource
import ru.asergeenko.schrandom.conf.ServiceProps
import pureconfig.generic.auto._
import ru.asergeenko.schrandom.controller.SchrandomLogicController
import ru.asergeenko.schrandom.tool.Logger

import scala.io.Source

object Application extends App with Logger {
  private val asciiLogo = Source.fromResource("logo.txt").getLines.mkString("\n")
  private val schrandomController: SchrandomLogicController.type = SchrandomLogicController
  private val config: Result[ServiceProps] = ConfigSource.default.load[ServiceProps]

  logger.debug(s"Schrandom is loaded with the following configuration: \n $config")
  logger.info("\n" + asciiLogo)
}
