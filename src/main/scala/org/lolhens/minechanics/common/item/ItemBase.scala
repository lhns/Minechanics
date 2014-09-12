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
  setUnlocalizedName(name)
  setCreativeTab(tab)

  GameRegistry.registerItem(this, name)

  override def getUnlocalizedName() = s"item.${Minechanics.Id.toLowerCase}:${
    val rawName = super.getUnlocalizedName
    rawName.substring(rawName.indexOf(".") + 1)
  }"

  override def getUnlocalizedName(stack: ItemStack) = getUnlocalizedName

  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IIconRegister) = iconRegister.registerIcon({
    val name = getUnlocalizedName
    name.substring(name.indexOf(".") + 1)
  })
}