package org.lolhens.minechanics.common.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

/**
 * Created by LolHens on 19.09.2014.
 */
class GuiHandler extends IGuiHandler {
  override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = ???

  override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = ???
}
