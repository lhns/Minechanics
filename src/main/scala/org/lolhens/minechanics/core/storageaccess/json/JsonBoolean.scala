package org.lolhens.minechanics.core.storageaccess.json

class JsonBoolean(override val obj: Boolean) extends ValidJsonObject[Boolean](obj) {
  override def getBooleanValue(): Boolean = obj
}