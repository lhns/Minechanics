package org.lolhens.minechanics.common.block

import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import org.lolhens.minechanics.Minechanics
import org.lolhens.minechanics.client.texture.Textures
import org.lolhens.minechanics.client.texture.Textures.IOnTextureRegistered
import org.lolhens.minechanics.common.block.material.MaterialCustom
import org.lolhens.minechanics.common.block.material.MaterialCustom.ICustomMaterial

class BlockBase(name: String) extends Block(new MaterialCustom()) with IOnTextureRegistered with ICustomMaterial {
  getMaterial.asInstanceOf[MaterialCustom].block = this
  val rawName = s"${Minechanics.Id}:$name"
  val unlocalizedName = s"tile.$rawName"

  setBlockName(name)
  setCreativeTab(CreativeTabs.tabMisc)

  def register = GameRegistry.registerBlock(this, name)

  override def getUnlocalizedName() = unlocalizedName

  override def getCanBlockGrass = super[ICustomMaterial].getCanBlockGrass

  override def getMobilityFlag = super[ICustomMaterial].getMobilityFlag

  override def isOpaqueCube = super[ICustomMaterial].isOpaqueCube

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(iconRegister: IIconRegister): Unit = {}

  @SideOnly(Side.CLIENT)
  override def onTextureRegistered = {
    val textureName = s"blocks.${name.toLowerCase}"
    if (Textures.icons contains textureName) blockIcon = Textures.icons(textureName)
  }
}