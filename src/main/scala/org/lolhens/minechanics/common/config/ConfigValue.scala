package org.lolhens.minechanics.common.config

import reflect.runtime.universe._
import net.minecraftforge.common.config.Configuration
import net.minecraft.block.Block
import scala.reflect.ClassTag
import org.lolhens.minechanics.common.util.UnlocalizedNameUtil

class ConfigValue(field: FieldMirror) {
  val default = field.get

  def delPostSpace(str: String) = if (str.charAt(str.length - 1) == ' ') str.substring(0, str.length - 1) else str

  private val fieldName = delPostSpace(field.symbol.name.toString)
  private val splitName = fieldName.split("_")
  val name = splitName(splitName.length - 1)
  val group = if (splitName.length > 1) splitName(0) else Configuration.CATEGORY_GENERAL

  def set(obj: Any) = field.set(obj);

  def isValid() = default match {
    case null => false
    case _: Boolean => true
    case _: Integer => true
    case _: Double => true
    case _: String => true
    case _: Block => true
  }

  def load(config: Configuration) = default match {
    case v: Boolean => set(config.get(group, name, v).getBoolean(v))
    case v: Integer => set(config.get(group, name, v).getInt)
    case v: Double => set(config.get(group, name, v).getDouble(v))
    case v: String => set(config.get(group, name, v).getString)
    case v: Block =>
      val defaultBlockName = v.getUnlocalizedName
      val configProperty = config.get(group, name, defaultBlockName)
      var loadedBlock = UnlocalizedNameUtil.getBlockByUnlocalizedName(configProperty.getString)
      if (loadedBlock == null) {
        loadedBlock = v
        configProperty.set(defaultBlockName)
      }
      set(loadedBlock)
  }
}