package org.lolhens.minechanics.core.json

class JsonDouble(val double: Double) extends ValidJsonObject[Double](double) {
  override def getDoubleValue(): Double = double
}