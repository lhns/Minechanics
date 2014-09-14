package org.lolhens.minechanics.core.storageaccess.json

import org.lolhens.minechanics.core.storageaccess._
import com.google.gson.Gson
import scala.io

object JsonObject extends EmptyStorageAccess {
  def fromAny(any: Any): StorageAccess = any match {
    case null => JsonObject
    case any: java.util.List[_] => new JsonList(any)
    case any: java.util.Map[_, _] => new JsonMap(any)
    case any: String => new JsonString(any)
    case any: Double => new JsonDouble(any)
    case any: Boolean => new JsonBoolean(any)
    case _ => JsonObject
  }

  def fromJson(string: String, tpe: Class[_ <: StorageAccess]): StorageAccess = fromAny(new Gson().fromJson(string, tpe match {
    case t if t == classOf[JsonMap] => classOf[java.util.Map[_, _]]
    case t if t == classOf[JsonList] => classOf[java.util.List[_]]
    case _ => return JsonObject
  }))

  def fromJson(string: String): StorageAccess = {
    val mapIndex = string.indexOf("{")
    val listIndex = string.indexOf("[")
    if (mapIndex >= 0 && mapIndex < listIndex)
      fromJson(string, classOf[JsonMap])
    else if (listIndex >= 0 && listIndex < mapIndex)
      fromJson(string, classOf[JsonList])
    else
      JsonObject
  }

  def fromFile(path: String): StorageAccess = fromJson(io.Source.fromFile(path).mkString)
}