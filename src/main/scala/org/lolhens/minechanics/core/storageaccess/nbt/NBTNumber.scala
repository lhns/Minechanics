package org.lolhens.minechanics.core.storageaccess.nbt

import net.minecraft.nbt._

class NBTNumber(override val obj: NBTBase.NBTPrimitive) extends ValidNBTObject[NBTBase.NBTPrimitive](obj) {
  override def getNumberValue() = getDoubleValue
  override def getDoubleValue(): Double = obj.func_150286_g
  override def getFloatValue(): Float = obj.func_150288_h
  override def getIntegerValue(): Integer = obj.func_150287_d
  override def getLongValue(): Long = obj.func_150291_c
  override def getByteValue(): Byte = obj.func_150290_f
  override def getShortValue(): Short = obj.func_150289_e
  override def getStringValue(): String = String.valueOf(getDoubleValue)
}