package ru.asergeenko.schrandom.settings

sealed abstract class GeneratorType(source: String)

object GeneratorType {
  case object BOUNDED extends GeneratorType("BOUNDED")
  case object UNBOUNDED extends GeneratorType("UNBOUNDED")
}
