package org.lolhens.minechanics.core.json

class JsonMap(val map: java.util.Map[_, _]) extends ValidJsonObject {
  override def get(key: String) = JsonObject.fromAny(map.get(key))
}