package org.lolhens.minechanics.common.item

import net.minecraftforge.fml.common.registry.GameRegistry
import org.lolhens.minechanics.Minechanics

@GameRegistry.ObjectHolder(Minechanics.Id)
object Items {
  val test = new ItemTest

  def register = {
    test.register
  }
}