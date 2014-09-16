package org.lolhens.minechanics.common.block

import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon

class BlockFoliage extends BlockBase("foliage", Material.grass, CreativeTabs.tabMaterials) {
  private var foliage_top, foliage_bottom: IIcon = null

  @SideOnly(Side.CLIENT)
  override def getIcon(side: Int, meta: Int) = side match {
    case 0 => foliage_bottom
    case 1 => foliage_top
    case _ => blockIcon
  }

  override def registerBlockIcons(iconRegister: IIconRegister) = {
    super.registerBlockIcons(iconRegister)
    foliage_top = iconRegister.registerIcon("minechanics:foliage_top")
    foliage_bottom = iconRegister.registerIcon("minecraft:dirt")
  }
}