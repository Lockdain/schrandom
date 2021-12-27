package ru.asergeenko.schrandom.controller

import akka.actor.ActorSystem

object ServiceController {
  private implicit val system = ActorSystem("api")
  private implicit val executionContext = system.getDispatcher


}
