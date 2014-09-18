package org.lolhens.minechanics.core.asm

import net.minecraft.launchwrapper.IClassTransformer

class ClassTransformer extends IClassTransformer {
  override def transform(name: String, deobfName: String, basicClass: Array[Byte]): Array[Byte] = {
    val transformation = Transformations.transformations.get(deobfName)
    if (transformation.nonEmpty) {
      val transformedClass = transformation.get.transform(basicClass)
      if (transformedClass != null) return transformedClass
    }
    basicClass
  }
}