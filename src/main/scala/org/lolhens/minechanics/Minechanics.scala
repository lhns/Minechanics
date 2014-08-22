package org.lolhens.minechanics

import cpw.mods.fml.common.Mod
import java.util.logging.Logger
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.Mod.EventHandler
import org.lolhens.minechanics.common.Proxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import cpw.mods.fml.common.SidedProxy

@Mod(modid = Minechanics.Id, name = Minechanics.Name, version = Minechanics.Version, modLanguage = "scala")
object Minechanics {
  final val Id = "minechanics"
  final val Name = "Minechanics3"
  final val Version = "@VERSION@"

  val log = Logger.getLogger(Id)

  @SidedProxy(clientSide = "org.lolhens.minechanics.client.ClientProxy", serverSide = "org.lolhens.minechanics.server.ServerProxy")
  var proxy: Proxy = null

  @EventHandler
  def preInit(event: FMLPreInitializationEvent) = proxy.preInit(event)

  @EventHandler
  def init(event: FMLInitializationEvent) = proxy.init(event)

  @EventHandler
  def postInit(event: FMLPostInitializationEvent) = proxy.postInit(event)

  @EventHandler
  def serverStart(event: FMLServerStartingEvent) = proxy.serverStart(event)
}