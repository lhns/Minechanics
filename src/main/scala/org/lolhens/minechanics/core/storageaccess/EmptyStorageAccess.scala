package org.lolhens.minechanics.core.storageaccess

abstract class EmptyStorageAccess extends StorageAccess {
  override def foreach(f: (StorageAccess) => Unit) = {}
  override def map[B](f: (Any) => B): Iterable[B] = List()

  override def get(key: String): StorageAccess = fromAny(null)

  override def getStringValue(): String = null
  def getNumberValue(): Number = 0
  override def get(): Any = null
  override def isValid() = false
}