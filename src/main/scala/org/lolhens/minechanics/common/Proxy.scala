package org.lolhens.minechanics.common

import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent, FMLServerStartingEvent}
import org.lolhens.minechanics.Minechanics
import org.lolhens.minechanics.common.block.Blocks
import org.lolhens.minechanics.common.config.{Config, Configurator}
import org.lolhens.minechanics.common.item.Items

class Proxy {
  def preInit(event: FMLPreInitializationEvent) = {
    Minechanics.configurator = new Configurator(event, Config)
    Minechanics.configurator.synch

    loadTextures

    Items.register
    Blocks.register
  }

  def init(event: FMLInitializationEvent) = {}

  def postInit(event: FMLPostInitializationEvent) = {}

  def serverStart(event: FMLServerStartingEvent) = {}

  def loadTextures = {}
}