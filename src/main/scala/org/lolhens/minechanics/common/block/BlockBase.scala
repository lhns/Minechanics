package org.lolhens.minechanics.common.block

import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import org.lolhens.minechanics.Minechanics
import org.lolhens.minechanics.client.texture.Textures
import org.lolhens.minechanics.client.texture.Textures.IOnTextureRegistered

class BlockBase(name: String, material: Material, tab: CreativeTabs) extends Block(material) with IOnTextureRegistered {
  val rawName = s"${Minechanics.Id}:$name"
  val unlocalizedName = s"tile.$rawName"

  setBlockName(name)

  setCreativeTab(tab)

  def this(name: String, tab: CreativeTabs) = this(name, Material.gourd, tab)

  def this(name: String) = this(name, CreativeTabs.tabMisc)

  def register = GameRegistry.registerBlock(this, name)

  override def getUnlocalizedName() = unlocalizedName

  @SideOnly(Side.CLIENT)
  override def onTextureRegistered = {
    val textureName = s"blocks.${name.toLowerCase}"
    if (Textures.icons contains textureName) blockIcon = Textures.icons(textureName)
  }
}