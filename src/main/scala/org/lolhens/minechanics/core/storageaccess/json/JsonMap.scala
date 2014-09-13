package org.lolhens.minechanics.core.storageaccess.json

class JsonMap(override val obj: java.util.Map[_, _]) extends ValidJsonObject[java.util.Map[_, _]](obj) {
  override def get(key: String) = JsonObject.fromAny(obj.get(key))
}