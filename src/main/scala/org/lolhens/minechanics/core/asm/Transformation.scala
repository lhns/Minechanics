package org.lolhens.minechanics.core.asm

import org.lolhens.minechanics.core.asm.Transformation._
import org.objectweb.asm.tree.InsnList

class Transformation(trigger: Trigger) {
  def transform(source: Array[Byte]): Array[Byte] = {
    null
  }
}

object Transformation {

  class Trigger(insns: InsnList, pre: Boolean) {

  }

}