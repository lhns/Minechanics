package org.lolhens.minechanics.core.json

import scala.language.dynamics

class JsonObject extends Dynamic {
  def apply(i: Int): JsonObject = new JsonObject

  def foreach(f: (JsonObject) => Unit) = {}

  def map[B](f: (Any) => B): Iterable[B] = List()

  final def selectDynamic(name: String): JsonObject = new JsonObject

  def get(key: String): JsonObject = new JsonObject

  def getStringValue(): String = null

  def getIntValue(): Int = 0

  def getDoubleValue(): Double = 0

  def isValid() = false
}

object JsonObject {
  def fromAny(any: Any): JsonObject = {
    any match {
      case null => new JsonObject
      case any: java.util.List[_] => new JsonList(any)
      case any: java.util.Map[_, _] => new JsonMap(any)
      case any: String => new JsonString(any)
      case any: Int => new JsonInt(any)
      case any: Double => new JsonDouble(any)
      case _ => new JsonObject
    }
  }

  implicit def getStringValue(json: JsonObject): String = json.getStringValue
  implicit def getIntValue(json: JsonObject): Int = json.getIntValue
  implicit def getDoubleValue(json: JsonObject): Double = json.getDoubleValue
}