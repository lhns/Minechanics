package org.lolhens.minechanics.common.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import org.lolhens.minechanics.Minechanics
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.common.registry.GameRegistry

class BlockBase(name: String, material: Material) extends Block(material) {
  private val unwrappedName = s"$name.name"
  private val unlocalizedName = s"tile.${Minechanics.Id.toLowerCase}:$unwrappedName"
  setBlockName(name);

  GameRegistry.registerBlock(this, name)

  def this(name: String) = this(name, Material.rock)

  override def getUnlocalizedName() = unlocalizedName

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(iconRegister: IIconRegister) = iconRegister.registerIcon(unwrappedName)
}