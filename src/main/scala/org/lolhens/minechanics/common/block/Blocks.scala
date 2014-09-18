package org.lolhens.minechanics.common.block

import cpw.mods.fml.common.registry.GameRegistry
import org.lolhens.minechanics.Minechanics

@GameRegistry.ObjectHolder(Minechanics.Id)
object Blocks {
  val foliage = new BlockFoliage
  val hay = new BlockHay
  val oil = new BlockOil

  def register = {
    foliage.register
    hay.register
    oil.register
  }
}