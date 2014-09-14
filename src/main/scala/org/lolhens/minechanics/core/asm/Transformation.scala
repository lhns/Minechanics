package org.lolhens.minechanics.core.asm

import org.objectweb.asm.tree.InsnList
import Transformation._

class Transformation(trigger: Trigger) {
  def transform(source: Array[Byte]): Array[Byte] = {
    null
  }
}

object Transformation {
  class Trigger(insns: InsnList, pre: Boolean) {

  }
}