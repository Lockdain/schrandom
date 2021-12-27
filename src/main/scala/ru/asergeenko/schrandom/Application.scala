package ru.asergeenko.schrandom

import org.slf4j.LoggerFactory
import ru.asergeenko.schrandom.controller.SchrandomController

object Application extends App {
  private val logger                           = LoggerFactory.getLogger(this.getClass.toString)

  private val controller: SchrandomController.type = SchrandomController
}
