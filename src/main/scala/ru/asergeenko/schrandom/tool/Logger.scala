package ru.asergeenko.schrandom.tool

import org.slf4j
import org.slf4j.LoggerFactory

trait Logger {
  val logger: slf4j.Logger = LoggerFactory.getLogger(loggerName)
  def loggerName: String = getClass.getName
}
