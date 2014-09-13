package org.lolhens.minechanics.core.storageaccess.nbt

import net.minecraft.nbt._

class NBTCompound(override val obj: NBTTagCompound) extends ValidNBTObject[NBTTagCompound](obj) {
  override def get(key: String) = NBTObject.fromAny(obj.getTag(key))
}