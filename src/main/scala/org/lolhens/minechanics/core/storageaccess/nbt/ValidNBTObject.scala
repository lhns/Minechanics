package org.lolhens.minechanics.core.storageaccess.nbt

import net.minecraft.nbt.NBTBase
import org.lolhens.minechanics.core.storageaccess.{StorageAccess, ValidStorageAccess}

class ValidNBTObject[T <: NBTBase](override val obj: T) extends ValidStorageAccess[T](obj) {
  def fromAny(any: Any): StorageAccess = NBTObject.fromAny(any)
}