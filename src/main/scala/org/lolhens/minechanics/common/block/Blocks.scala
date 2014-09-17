package org.lolhens.minechanics.common.block

import cpw.mods.fml.common.registry.GameRegistry
import org.lolhens.minechanics.Minechanics

@GameRegistry.ObjectHolder(Minechanics.Id)
object Blocks {
  val foliage = new BlockFoliage
  val hay = new BlockHay

  def register = {
    foliage.register
    hay.register
  }
}