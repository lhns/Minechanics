package org.lolhens.minechanics.client.config.gui

import net.minecraftforge.fml.client.config.{ConfigGuiType, GuiConfig, IConfigElement}
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.{ConfigElement, Property}
import org.lolhens.minechanics.Minechanics
import org.lolhens.minechanics.client.config.gui.element.{ConfigElementBlock, ConfigElementLabel}
import org.lolhens.minechanics.common.config.Configurator
import java.util

import scala.collection.JavaConversions._

class ConfigGui(parent: GuiScreen) extends GuiConfig(parent, new java.util.ArrayList(), Minechanics.Id, false, false, Minechanics.Name) {
  addGuiElements(configElements.asInstanceOf[util.List[ConfigElement[_]]], Minechanics.configurator)
  FMLCommonHandler.instance.bus.register(this)

  def addGuiElements(elements: java.util.List[IConfigElement[_]], configurator: Configurator[_]) = {
    def firstLetterToUpperCase(string: String) = if (string.length > 1) string.substring(0, 1).toUpperCase + string.substring(1) else string
    val config = configurator.getConfig
    val iterator = config.getCategoryNames.iterator
    while (iterator.hasNext) {
      val categoryName = iterator.next
      val category = config.getCategory(categoryName)
      elements.add(new ConfigElementLabel(category, firstLetterToUpperCase(categoryName)))
      elements.addAll(for (element <- new ConfigElement[Object](category).getChildElements()) yield element match {
        case element if element.isProperty && element.getType == ConfigGuiType.STRING && element.getName.startsWith("block:") =>
          new ConfigElementBlock(ConfigGui.ConfigElementPropField.get(element).asInstanceOf[Property])
        case element => element
      })
    }
  }

  @SubscribeEvent
  def configChanged(event: ConfigChangedEvent.OnConfigChangedEvent) = Minechanics.configurator.synch
}

object ConfigGui {
  val ConfigElementPropField = classOf[ConfigElement[_]].getDeclaredField("prop")
  ConfigElementPropField.setAccessible(true)
}