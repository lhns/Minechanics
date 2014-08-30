package org.lolhens.minechanics.core.json

protected abstract class ValidJsonObject[T](val obj: T) extends JsonObject {
  override def getStringValue(): String = String.valueOf(obj)
  
  override final def isValid() = true
}