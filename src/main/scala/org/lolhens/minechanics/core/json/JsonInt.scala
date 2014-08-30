package org.lolhens.minechanics.core.json

class JsonInt(val int: Int) extends ValidJsonObject {
  override def getIntValue(): Int = int
}