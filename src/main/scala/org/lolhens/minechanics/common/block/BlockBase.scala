package org.lolhens.minechanics.common.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import org.lolhens.minechanics.Minechanics
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.creativetab.CreativeTabs

class BlockBase(name: String, material: Material, tab: CreativeTabs) extends Block(material) {
  private val rawName = s"${Minechanics.Id}:$name"
  private val unlocalizedName = s"tile.$rawName"

  setBlockName(name)

  setCreativeTab(tab)

  def this(name: String, tab: CreativeTabs) = this(name, Material.rock, tab)
  def this(name: String) = this(name, CreativeTabs.tabMisc)

  def register() = GameRegistry.registerBlock(this, name)

  override def getUnlocalizedName() = unlocalizedName

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(iconRegister: IIconRegister) = blockIcon = iconRegister.registerIcon(rawName)
}