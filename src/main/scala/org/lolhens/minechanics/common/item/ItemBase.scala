package org.lolhens.minechanics.common.item

import net.minecraft.item.Item
import org.lolhens.minechanics.Minechanics
import net.minecraft.item.ItemStack
import net.minecraft.client.renderer.texture.IIconRegister
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.util.IIcon
import net.minecraft.entity.player.EntityPlayer
import org.lolhens.minechanics.client.texture.Textures
import ItemBase._

class ItemBase(name: String, tab: CreativeTabs) extends Item {
  val rawName = s"${Minechanics.Id}:$name"
  val unlocalizedName = s"item.$rawName"

  setUnlocalizedName(name)

  setCreativeTab(tab)
  setNoRepair

  def this(name: String) = this(name, CreativeTabs.tabMisc)

  def register() = GameRegistry.registerItem(this, name)

  override def getUnlocalizedName() = unlocalizedName

  override def getUnlocalizedName(stack: ItemStack) = getUnlocalizedName

  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IIconRegister) = {
    if (itemsRegistered == null || itemsRegistered == this) {
      //Resources.registerIcons("items", iconRegister)
      itemsRegistered = this

    }
    //if (Resources.icons contains s"items.$name") itemIcon = Resources.icons(s"items.$name")
  }
}

object ItemBase {
  private var itemsRegistered: ItemBase = null
}