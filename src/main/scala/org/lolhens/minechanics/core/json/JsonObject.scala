package org.lolhens.minechanics.core.json

import scala.language.dynamics
import com.google.gson.Gson
import scala.io

class JsonObject extends Dynamic {
  final def selectDynamic(name: String): JsonObject = {
    if (name.matches("_\\d+"))
      apply(name.substring(1).toInt)
    else
      get(name)
  }

  def apply(i: Int): JsonObject = get(String.valueOf(i))

  def foreach(f: (JsonObject) => Unit) = {}

  def map[B](f: (Any) => B): Iterable[B] = List()

  def get(key: String): JsonObject = new JsonObject

  def getStringValue(): String = null

  def getDoubleValue(): Double = 0

  def getBooleanValue(): Boolean = false

  def isValid() = false
}

object JsonObject {
  def fromAny(any: Any): JsonObject = {
    any match {
      case null => new JsonObject
      case any: java.util.List[_] => new JsonList(any)
      case any: java.util.Map[_, _] => new JsonMap(any)
      case any: String => new JsonString(any)
      case any: Double => new JsonDouble(any)
      case any: Boolean => new JsonBoolean(any);
      case _ => new JsonObject
    }
  }

  def fromJson(string: String, tpe: Class[_ <: JsonObject]): JsonObject = fromAny(new Gson().fromJson(string, tpe match {
    case t if t == classOf[JsonMap] => classOf[java.util.Map[_, _]]
    case t if t == classOf[JsonList] => classOf[java.util.List[_]]
    case _ => return new JsonObject()
  }))

  def fromJson(string: String): JsonObject = {
    val mapIndex = string.indexOf("{")
    val listIndex = string.indexOf("[")
    if (mapIndex >= 0 && mapIndex < listIndex)
      fromJson(string, classOf[JsonMap])
    else if (listIndex >= 0 && listIndex < mapIndex)
      fromJson(string, classOf[JsonList])
    else
      new JsonObject()
  }

  def fromFile(path: String): JsonObject = fromJson(io.Source.fromFile(path).mkString)

  implicit def getStringValue(json: JsonObject): String = json.getStringValue
  implicit def getDoubleValue(json: JsonObject): Double = json.getDoubleValue
  implicit def getBooleanValue(json: JsonObject): Boolean = json.getBooleanValue
}