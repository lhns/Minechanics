package org.lolhens.minechanics.common.item

import net.minecraftforge.fml.common.registry.GameRegistry

//import net.minecraft.client.renderer.texture.IIconRegister

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import org.lolhens.minechanics.Minechanics
import org.lolhens.minechanics.client.texture.Textures.IOnTextureRegistered

class ItemBase(name: String) extends Item with IOnTextureRegistered {
  val rawName = s"${Minechanics.Id}:$name"
  val unlocalizedName = s"item.$rawName"

  setUnlocalizedName(name)
  setCreativeTab(CreativeTabs.tabMisc)

  def register = GameRegistry.registerItem(this, name)

  override def getUnlocalizedName = unlocalizedName

  override def getUnlocalizedName(stack: ItemStack) = getUnlocalizedName

  /*@SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IIconRegister): Unit = {}

  @SideOnly(Side.CLIENT)
  override def onTextureRegistered = {
    val textureName = s"items.${name.toLowerCase}"
    if (Textures.icons contains textureName) itemIcon = Textures.icons(textureName)
  }*/
  override def onTextureRegistered: Unit = ???
}