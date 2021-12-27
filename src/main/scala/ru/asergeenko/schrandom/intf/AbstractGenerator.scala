package ru.asergeenko.schrandom.intf

import ru.asergeenko.schrandom.settings.GeneratorBehavior

trait AbstractGenerator {
  def run
  def getBehavior(): GeneratorBehavior
}
