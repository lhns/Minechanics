package org.lolhens.minechanics.core.storageaccess.nbt

import net.minecraft.nbt._

class NBTCompound(override val obj: NBTTagCompound) extends ValidNBTObject[NBTTagCompound](obj) {
  override def apply(key: String) = NBTObject.fromAny(obj.getTag(key))
}