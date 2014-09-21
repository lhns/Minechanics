package org.lolhens.minechanics.common.block

import net.minecraft.block.Block

/**
 * Created by LolHens on 18.09.2014.
 */
class BlockOil extends BlockBase("Oil") {
  setStepSound(Block.soundTypeSnow)

  override def getRenderBlockPass: Int = 1

  override def isOpaqueCube = false
}
