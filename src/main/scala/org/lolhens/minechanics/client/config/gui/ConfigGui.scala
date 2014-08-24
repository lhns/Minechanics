package org.lolhens.minechanics.client.config.gui

import cpw.mods.fml.client.config.GuiConfig
import net.minecraft.client.gui.GuiScreen
import cpw.mods.fml.client.config.IConfigElement
import org.lolhens.minechanics.common.config.Configurator
import org.lolhens.minechanics.client.config.gui.element.LabelConfigElement
import net.minecraftforge.common.config.ConfigElement
import org.lolhens.minechanics.Minechanics
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.client.event.ConfigChangedEvent
import org.lolhens.minechanics.common.util.LogHelper

class ConfigGui(parent: GuiScreen) extends GuiConfig(parent, new java.util.ArrayList(), Minechanics.Id, false, false, Minechanics.Name) {
  addGuiElements(configElements, Minechanics.configurator)
  FMLCommonHandler.instance().bus().register(this)

  def addGuiElements(elements: java.util.List[IConfigElement[_]], configurator: Configurator[_]) = {
    def firstLetterToUpperCase(string: String) = if (string.length() > 1) string.substring(0, 1).toUpperCase() + string.substring(1) else string;
    val config = configurator.getConfig()
    val iterator = config.getCategoryNames().iterator()
    while (iterator.hasNext()) {
      val categoryName = iterator.next()
      val category = config.getCategory(categoryName)
      elements.add(new LabelConfigElement(category, firstLetterToUpperCase(categoryName)))
      elements.addAll(new ConfigElement[Object](category).getChildElements())
    }
  }

  @SubscribeEvent
  def configChanged(event: ConfigChangedEvent.OnConfigChangedEvent) = Minechanics.configurator.synch()
}