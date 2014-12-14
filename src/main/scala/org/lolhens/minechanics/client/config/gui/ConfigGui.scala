package org.lolhens.minechanics.client.config.gui

import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.config.GuiConfig
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lolhens.minechanics.Minechanics

class ConfigGui(parent: GuiScreen) extends GuiConfig(parent, new java.util.ArrayList(), Minechanics.Id, false, false, Minechanics.Name) {
  FMLCommonHandler.instance.bus.register(this)

  @SubscribeEvent
  def configChanged(event: ConfigChangedEvent.OnConfigChangedEvent) = Minechanics.configurator.synch
}