package org.lolhens.minechanics.core.storageaccess.nbt

import org.lolhens.minechanics.core.storageaccess.ValidStorageAccess
import org.lolhens.minechanics.core.storageaccess.StorageAccess

class ValidNBTObject[T](override val obj: T) extends ValidStorageAccess[T](obj) {
  def fromAny(any: Any): StorageAccess = NBTObject.fromAny(any)
}