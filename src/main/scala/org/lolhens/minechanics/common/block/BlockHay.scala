package org.lolhens.minechanics.common.block

import java.util.Random

import net.minecraft.entity.Entity
import net.minecraft.init.Items
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.World

class BlockHay extends BlockBase("hay") {
  setHardness(1)

  override def isOpaqueCube = false

  override def onFallenUpon(world: World, x: Int, y: Int, z: Int, entity: Entity, speed: Float): Unit = if (speed > 2) {
    world.setBlockToAir(x, y, z)
    dropBlockAsItem(world, x, y, z, new ItemStack(Items.wheat, 4))
    entity.fallDistance = 0
  }


  override def getItemDropped(p_149650_1_ : Int, p_149650_2_ : Random, p_149650_3_ : Int): Item = Items.wheat

  override def quantityDropped(p_149745_1_ : Random): Int = 4
}