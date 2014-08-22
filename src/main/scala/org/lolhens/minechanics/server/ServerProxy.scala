package org.lolhens.minechanics.server

import org.lolhens.minechanics.common.Proxy
import cpw.mods.fml.common.event.FMLServerStartingEvent

class ServerProxy extends Proxy {
  override def serverStart(event: FMLServerStartingEvent) = {}
}