package org.lolhens.minechanics.common.item

import cpw.mods.fml.common.registry.GameRegistry
import org.lolhens.minechanics.Minechanics

@GameRegistry.ObjectHolder(Minechanics.Id)
object Items {
  val test = new ItemTest

  def load = {}
}