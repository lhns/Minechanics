package org.lolhens.minechanics.core.json

class JsonMap(val map: java.util.Map[_, _]) extends ValidJsonObject[java.util.Map[_, _]](map) {
  override def get(key: String) = JsonObject.fromAny(map.get(key))
}