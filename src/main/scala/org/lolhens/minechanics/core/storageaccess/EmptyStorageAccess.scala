package org.lolhens.minechanics.core.storageaccess

abstract class EmptyStorageAccess extends StorageAccess {
  override def foreach(f: (StorageAccess) => Unit) = {}
  override def map[B](f: (Any) => B): Iterable[B] = List()

  override def get(key: String): StorageAccess = fromAny(null)

  override def getStringValue(): String = null
  override def getDoubleValue(): Double = 0
  override def getBooleanValue(): Boolean = false

  override def isValid() = false
}