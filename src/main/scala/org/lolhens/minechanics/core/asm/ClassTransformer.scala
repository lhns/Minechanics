package org.lolhens.minechanics.core.asm

import net.minecraft.launchwrapper.IClassTransformer

class ClassTransformer extends IClassTransformer {
  override def transform(name: String, deobfName: String, basicClass: Array[Byte]) = basicClass;
}