package org.lolhens.minechanics.common.util

import net.minecraft.block.Block

object UnlocalizedNameUtil {
  def getBlockByUnlocalizedName(unlocalizedNameArg: String): Block = {
    val unlocalizedName = unlocalizedNameArg.toLowerCase()
    val iterator = Block.blockRegistry.iterator()
    while (iterator.hasNext()) {
      val obj = iterator.next()
      if (obj.isInstanceOf[Block] && obj.asInstanceOf[Block].getUnlocalizedName().toLowerCase() == unlocalizedName)
        return obj.asInstanceOf[Block]
    }
    null
  }
}