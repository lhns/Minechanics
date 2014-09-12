package org.lolhens.minechanics.common.item

import net.minecraft.item.Item
import org.lolhens.minechanics.Minechanics
import net.minecraft.item.ItemStack
import net.minecraft.client.renderer.texture.IIconRegister
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.creativetab.CreativeTabs

class ItemBase(name: String, tab: CreativeTabs) extends Item {
  private val unwrappedName = s"$name.name"
  private val unlocalizedName = s"tile.${Minechanics.Id.toLowerCase}:${unwrappedName}"
  setUnlocalizedName(name)

  setCreativeTab(tab)

  GameRegistry.registerItem(this, name)

  def this(name: String) = this(name, CreativeTabs.tabMisc)

  override def getUnlocalizedName() = s"item.${Minechanics.Id.toLowerCase}:$unlocalizedName"

  override def getUnlocalizedName(stack: ItemStack) = getUnlocalizedName

  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IIconRegister) = iconRegister.registerIcon(unwrappedName)
}