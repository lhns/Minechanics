package org.lolhens.minechanics.core.json

class JsonString(val string: String) extends ValidJsonObject {
  override def getStringValue(): String = string
}