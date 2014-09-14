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
  def getNumberValue(): Number
  def getDoubleValue(): Double = getNumberValue.doubleValue
  def getFloatValue(): Float = getNumberValue.floatValue
  def getIntegerValue(): Integer = getNumberValue.intValue
  def getLongValue(): Long = getNumberValue.longValue
  def getByteValue(): Byte = getNumberValue.byteValue
  def getShortValue(): Short = getNumberValue.shortValue
  def getBooleanValue(): Boolean = getNumberValue.intValue != 0
  def get(): Any
  def isValid(): Boolean

  def fromAny(any: Any): StorageAccess
}

object StorageAccess {
  implicit def getStringValue(storageAccess: StorageAccess): String = storageAccess.getStringValue
  implicit def getDoubleValue(storageAccess: StorageAccess): Double = storageAccess.getDoubleValue
  implicit def getFloatValue(storageAccess: StorageAccess): Float = storageAccess.getFloatValue
  implicit def getIntegerValue(storageAccess: StorageAccess): Integer = storageAccess.getIntegerValue
  implicit def getLongValue(storageAccess: StorageAccess): Long = storageAccess.getLongValue
  implicit def getByteValue(storageAccess: StorageAccess): Byte = storageAccess.getByteValue
  implicit def getShortValue(storageAccess: StorageAccess): Short = storageAccess.getShortValue
  implicit def getBooleanValue(storageAccess: StorageAccess): Boolean = storageAccess.getBooleanValue
}