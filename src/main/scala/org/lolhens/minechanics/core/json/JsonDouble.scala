package org.lolhens.minechanics.core.json

class JsonDouble(val double: Double) extends ValidJsonObject {
  override def getDoubleValue(): Double = double
}