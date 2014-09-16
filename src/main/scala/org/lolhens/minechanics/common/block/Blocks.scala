package org.lolhens.minechanics.common.block

import cpw.mods.fml.common.registry.GameRegistry
import org.lolhens.minechanics.Minechanics

@GameRegistry.ObjectHolder(Minechanics.Id)
object Blocks {
  val foliage = new BlockFoliage
  val test = new BlockTest

  def register = {
    foliage.register
    test.register
  }
}