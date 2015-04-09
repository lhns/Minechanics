package org.lolhens.minechanics.client.config.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraftforge.fml.client.IModGuiFactory.{RuntimeOptionCategoryElement, RuntimeOptionGuiHandler}

class ConfigGuiFactory extends IModGuiFactory {
  override def getHandlerFor(arg0: RuntimeOptionCategoryElement): RuntimeOptionGuiHandler = null

  override def initialize(arg0: Minecraft) = {}

  override def mainConfigGuiClass(): Class[_ <: GuiScreen] = classOf[ConfigGui]

  override def runtimeGuiCategories(): java.util.Set[RuntimeOptionCategoryElement] = null
}