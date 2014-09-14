package org.lolhens.minechanics.core.storageaccess.nbt

import org.lolhens.minechanics.core.storageaccess.EmptyStorageAccess
import org.lolhens.minechanics.core.storageaccess.StorageAccess
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagIntArray
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagByteArray
import net.minecraft.nbt.NBTTagString

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