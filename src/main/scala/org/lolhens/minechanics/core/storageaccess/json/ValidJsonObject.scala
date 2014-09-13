package org.lolhens.minechanics.core.storageaccess.json

import org.lolhens.minechanics.core.storageaccess._

abstract class ValidJsonObject[T](override val obj: T) extends ValidStorageAccess[T](obj) {
  def fromAny(any: Any): StorageAccess = JsonObject.fromAny(any)
}