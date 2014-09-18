package org.lolhens.minechanics.core.storageaccess

abstract class ValidStorageAccess[T](val obj: T) extends EmptyStorageAccess {
  override def getStringValue: String = String.valueOf(obj)
  override def get: T = obj
  override final def isValid = true
}