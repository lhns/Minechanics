package org.lolhens.minechanics.core

import java.io.File

import net.minecraftforge.fml.relauncher.{IFMLCallHook, IFMLLoadingPlugin}
import org.lolhens.minechanics.Minechanics
import org.lolhens.minechanics.core.FMLMinechanicsPlugin._
import org.lolhens.minechanics.core.asm.ClassTransformer
import org.lolhens.minechanics.core.storageaccess.json.JsonObject
import org.lolhens.minechanics.core.util.LogHelper

@IFMLLoadingPlugin.Name(Minechanics.Name)
@IFMLLoadingPlugin.MCVersion(Minechanics.McVersion)
@IFMLLoadingPlugin.TransformerExclusions(Array("org.lolhens.minechanics.core"))
class FMLMinechanicsPlugin extends IFMLLoadingPlugin {
  override def getASMTransformerClass = Array(classOf[ClassTransformer].getName)

  override def getModContainerClass = null

  override def getAccessTransformerClass = null

  override def getSetupClass = classOf[SetupClass].getName

  override def injectData(data: java.util.Map[String, Object]) = {}
}

object FMLMinechanicsPlugin {
  var location: File = null;

  class SetupClass extends IFMLCallHook {
    override def call = {
      LogHelper.info("coremod loaded")

      val root = JsonObject.fromFile("E:/test.json")
      LogHelper.fatal(root._0)
      LogHelper.fatal(root._2)
      LogHelper.fatal(root._3)
      null
    }

    override def injectData(data: java.util.Map[String, Object]) = location = data.get("coremodLocation").asInstanceOf[File]
  }

  class AccessTransformer extends cpw.mods.fml.common.asm.transformers.AccessTransformer(Minechanics.AccessTransformer) {}

}