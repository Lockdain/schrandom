package ru.asergeenko.schrandom.generator

import ru.asergeenko.schrandom.intf.AbstractGenerator
import ru.asergeenko.schrandom.settings.GeneratorBehavior

class BoundedGenerator(behavior: GeneratorBehavior) extends AbstractGenerator with Runnable {
  val eventFlooder = new EventFlooder(behavior.schema)

  override def run: Unit = {

  }

  override def getBehavior(): GeneratorBehavior = behavior
}
