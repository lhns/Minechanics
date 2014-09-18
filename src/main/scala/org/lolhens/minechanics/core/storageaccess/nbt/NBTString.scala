package org.lolhens.minechanics.core.storageaccess.nbt

import net.minecraft.nbt.NBTTagString

class NBTString(override val obj: NBTTagString) extends ValidNBTObject[NBTTagString](obj) {
  override def getStringValue: String = obj.func_150285_a_
}