package org.lolhens.minechanics.core

import org.lolhens.minechanics.Minechanics
import cpw.mods.fml.relauncher.IFMLLoadingPlugin
import java.io.File
import cpw.mods.fml.relauncher.IFMLCallHook
import org.lolhens.minechanics.core.util.LogHelper
import org.lolhens.minechanics.core.asm.ClassTransformer
import FMLMinechanicsPlugin._
import com.google.gson.Gson
import scala.io
import org.lolhens.minechanics.core.storageaccess.json.JsonObject

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