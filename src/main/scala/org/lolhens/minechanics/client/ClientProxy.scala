package org.lolhens.minechanics.client

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.common.MinecraftForge
import org.lolhens.minechanics.client.texture.Textures
import org.lolhens.minechanics.common.Proxy

class ClientProxy extends Proxy {


  override def preInit(event: FMLPreInitializationEvent) = {
    MinecraftForge.EVENT_BUS.register(Textures)
    super.preInit(event)
  }

  override def loadTextures: Unit = {
  }
}