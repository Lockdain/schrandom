package ru.asergeenko.schrandom.intf

import ru.asergeenko.schrandom.adt.GeneratorBehavior

trait AbstractGenerator extends Runnable {
  def run()
  def getBehavior: GeneratorBehavior
  def run(eventQty: Int)
}
