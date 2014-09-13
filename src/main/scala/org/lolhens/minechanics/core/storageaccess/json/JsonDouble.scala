package org.lolhens.minechanics.core.storageaccess.json

class JsonDouble(override val obj: Double) extends ValidJsonObject[Double](obj) {
  override def getDoubleValue(): Double = obj
}