package org.lolhens.minechanics.core.json

import scala.collection.JavaConversions._

class JsonList(val list: java.util.List[_]) extends ValidJsonObject {
  override def apply(i: Int) = if (i >= list.size || i < 0) new JsonObject else JsonObject.fromAny(list.get(i))

  override def foreach(f: JsonObject => Unit) = {
    val iterator = list.iterator();
    while (iterator.hasNext()) f(JsonObject.fromAny(iterator.next()))
  }

  override def map[B](f: (Any) => B): Set[B] = list.toSet[Any].map(f)
}