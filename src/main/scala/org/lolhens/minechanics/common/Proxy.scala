package org.lolhens.minechanics.common

import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import org.lolhens.minechanics.Minechanics
import org.lolhens.minechanics.common.config.Configurator
import org.lolhens.minechanics.common.config.Config

class Proxy {
  def preInit(event: FMLPreInitializationEvent) = {
    Minechanics.configurator = new Configurator(event, Config);
    Minechanics.configurator.synch();
  }

  def init(event: FMLInitializationEvent) = {}

  def postInit(event: FMLPostInitializationEvent) = {}

  def serverStart(event: FMLServerStartingEvent) = {}
}