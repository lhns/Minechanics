package org.lolhens.minechanics.core.storageaccess.nbt

import net.minecraft.nbt.{NBTBase, NBTTagByteArray, NBTTagCompound, NBTTagIntArray, NBTTagList, NBTTagString}
import org.lolhens.minechanics.core.storageaccess.{EmptyStorageAccess, StorageAccess}

object NBTObject extends EmptyStorageAccess {
  def fromAny(any: Any): StorageAccess = any match {
    case null => NBTObject
    case any: NBTTagCompound => new NBTCompound(any)
    case any: NBTTagList => new NBTList(any)
    case any: NBTBase.NBTPrimitive => new NBTNumber(any)
    case any: NBTTagString => new NBTString(any)
    case any: NBTTagIntArray => new NBTIntArray(any)
    case any: NBTTagByteArray => new NBTByteArray(any)
    case _ => NBTObject
  }
}