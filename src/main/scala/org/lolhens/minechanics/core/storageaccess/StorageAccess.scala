package org.lolhens.minechanics.core.storageaccess

import scala.language.dynamics

trait StorageAccess extends Dynamic {
  final def selectDynamic(name: String): StorageAccess = {
    if (name.matches("_\\d+"))
      apply(name.substring(1).toInt)
    else
      get(name)
  }
  def apply(i: Int): StorageAccess = get(String.valueOf(i))

  def foreach(f: (StorageAccess) => Unit)
  def map[B](f: (Any) => B): Iterable[B]

  def get(key: String): StorageAccess

  def getStringValue(): String
  def getDoubleValue(): Double
  def getBooleanValue(): Boolean

  def isValid(): Boolean

  def fromAny(any: Any): StorageAccess
}

object StorageAccess {
  implicit def getStringValue(storageAccess: StorageAccess): String = storageAccess.getStringValue
  implicit def getDoubleValue(storageAccess: StorageAccess): Double = storageAccess.getDoubleValue
  implicit def getBooleanValue(storageAccess: StorageAccess): Boolean = storageAccess.getBooleanValue
}