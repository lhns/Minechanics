package org.lolhens.minechanics.client.config.gui

import cpw.mods.fml.client.IModGuiFactory
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionCategoryElement
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionGuiHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

class ConfigGuiFactory extends IModGuiFactory {
  override def getHandlerFor(arg0: RuntimeOptionCategoryElement): RuntimeOptionGuiHandler = null
  override def initialize(arg0: Minecraft) = {}
  override def mainConfigGuiClass(): Class[_ <: GuiScreen] = classOf[ConfigGui]
  override def runtimeGuiCategories(): java.util.Set[RuntimeOptionCategoryElement] = null
}