package org.lolhens.minechanics.common.block

import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
//import net.minecraft.client.renderer.texture.IIconRegister
//import net.minecraft.util.IIcon

class BlockFoliage extends BlockBase("foliage") {
  setStepSound(Block.soundTypeGrass)

  //private var foliage_top, foliage_bottom: IIcon = null

  /*@SideOnly(Side.CLIENT)
  override def getIcon(side: Int, meta: Int) = side match {
    case 0 => foliage_bottom
    case 1 => foliage_top
    case _ => blockIcon
  }

  override def registerBlockIcons(iconRegister: IIconRegister) = {
    super.registerBlockIcons(iconRegister)
    foliage_top = iconRegister.registerIcon("minechanics:foliage_top")
    foliage_bottom = iconRegister.registerIcon("minecraft:dirt")
  }*/
  override def onTextureRegistered: Unit = ???
}