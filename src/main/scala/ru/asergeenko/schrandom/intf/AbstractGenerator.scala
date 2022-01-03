package ru.asergeenko.schrandom.intf

import ru.asergeenko.schrandom.settings.GeneratorBehavior

trait AbstractGenerator extends Runnable {
  def run()
  def getBehavior: GeneratorBehavior
  def run(eventQty: Int)
}
