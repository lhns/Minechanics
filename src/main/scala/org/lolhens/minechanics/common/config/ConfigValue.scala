package org.lolhens.minechanics.common.config

import net.minecraft.block.Block
import net.minecraftforge.common.config.Configuration
import org.lolhens.minechanics.common.util.UnlocalizedNameUtil
import org.lolhens.minechanics.core.util.LogHelper

import scala.reflect.runtime.universe._

class ConfigValue(field: FieldMirror) {
  val default = field.get

  def delPostSpace(str: String) = if (str.charAt(str.length - 1) == ' ') str.substring(0, str.length - 1) else str

  private val fieldName = delPostSpace(field.symbol.name.toString)
  private val splitName = fieldName.split("_")
  val name = splitName(splitName.length - 1)
  val group = if (splitName.length > 1) splitName(0) else Configuration.CATEGORY_GENERAL

  def set(obj: Any) = field.set(obj);

  def load(config: Configuration) = default match {
    case d: Boolean => set(config.get(group, name, d).getBoolean(d))
    case d: Int => set(config.get(group, name, d).getInt)
    case d: Double => set(config.get(group, name, d).getDouble(d))
    case d: String => set(config.get(group, name, d).getString)
    case d: Block =>
      val defaultBlockName = d.getUnlocalizedName
      val configProperty = config.get(group, "block:" + name, defaultBlockName)
      set(UnlocalizedNameUtil.getBlockByUnlocalizedName(configProperty.getString) match {
        case null =>
          configProperty.set(defaultBlockName)
          d
        case value => value
      })
  }
}

object ConfigValue {
  def isValidType(tpe: Type) = tpe match {
    case null => false
    case t if t =:= typeOf[Boolean] => true
    case t if t =:= typeOf[Int] => true
    case t if t =:= typeOf[Double] => true
    case t if t =:= typeOf[String] => true
    case t if t =:= typeOf[Block] => true
    case t =>
      LogHelper.fatal(t)
      false
  }
}