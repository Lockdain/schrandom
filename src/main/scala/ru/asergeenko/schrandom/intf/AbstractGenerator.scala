package ru.asergeenko.schrandom.intf

import ru.asergeenko.schrandom.settings.GeneratorBehavior

trait AbstractGenerator {
  def setBehavior(behavior: GeneratorBehavior)
  def run
  def getBehavior(): GeneratorBehavior
}
