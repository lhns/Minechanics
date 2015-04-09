package org.lolhens.minechanics.common.block

import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
//import net.minecraft.client.renderer.RenderBlocks
//import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.world.IBlockAccess
import org.lolhens.minechanics.Minechanics
import org.lolhens.minechanics.client.texture.Textures
import org.lolhens.minechanics.client.texture.Textures.IOnTextureRegistered
import org.lolhens.minechanics.common.block.material.MaterialCustom
import org.lolhens.minechanics.common.block.material.MaterialCustom.ICustomMaterial

class BlockBase(name: String) extends Block(new MaterialCustom()) with IOnTextureRegistered with ICustomMaterial {
  getMaterial.asInstanceOf[MaterialCustom].block = this

  val rawName = s"${Minechanics.Id}:$name"
  val unlocalizedName = s"tile.$rawName"
  setUnlocalizedName(name)

  setCreativeTab(CreativeTabs.tabMisc)

  def register = {
    GameRegistry.registerBlock(this, name)
  }

  override def getUnlocalizedName() = unlocalizedName

  override def getCanBlockGrass = super[ICustomMaterial].getCanBlockGrass

  override def getMobilityFlag = super[ICustomMaterial].getMobilityFlag

  override def isOpaqueCube = super[ICustomMaterial].isOpaqueCube

  override def onTextureRegistered: Unit = ???
}