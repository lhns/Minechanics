package org.lolhens.minechanics.core.storageaccess.nbt

import net.minecraft.nbt._

class NBTNumber(override val obj: NBTBase.NBTPrimitive) extends ValidNBTObject[NBTBase.NBTPrimitive](obj) {
  override def getNumberValue = getDoubleValue

  override def getDoubleValue: Double = obj.getDouble

  override def getFloatValue: Float = obj.getFloat

  override def getIntegerValue: Int = obj.getInt

  override def getLongValue: Long = obj.getLong

  override def getByteValue: Byte = obj.getByte

  override def getShortValue: Short = obj.getShort

  override def getStringValue: String = String.valueOf(getDoubleValue)
}