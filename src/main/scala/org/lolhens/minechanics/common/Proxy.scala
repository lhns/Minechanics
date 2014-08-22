package org.lolhens.minechanics.common

import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent

class Proxy {
  def preInit(event: FMLPreInitializationEvent) = {}

  def init(event: FMLInitializationEvent) = {}

  def postInit(event: FMLPostInitializationEvent) = {}
  
  def serverStart(event: FMLServerStartingEvent) = {}
}