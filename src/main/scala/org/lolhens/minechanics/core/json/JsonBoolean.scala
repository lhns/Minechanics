package org.lolhens.minechanics.core.json

class JsonBoolean(boolean: Boolean) extends ValidJsonObject[Boolean](boolean) {
  override def getBooleanValue(): Boolean = boolean
}