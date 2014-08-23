package org.lolhens.minechanics.common.config

import net.minecraftforge.common.config.Configuration
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import scala.collection.mutable.MutableList
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import org.lolhens.minechanics.Minechanics
import com.dafttech.primitive.PrimitiveUtil
import net.minecraft.block.Block
import org.lolhens.minechanics.common.util.LogHelper
import org.lolhens.minechanics.common.util.UnlocalizedNameUtil

class Configurator(config: Configuration, configClass: Class[_]) {
  def this(event: FMLPreInitializationEvent, configClass: Class[_]) = this(new Configuration(event.getSuggestedConfigurationFile()), configClass)

  val values = new MutableList[ConfigValue]()
  val subConfigurators = new MutableList[Configurator]()

  for (field <- configClass.getFields()) {
    val modifiers = field.getModifiers();
    if (Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
      LogHelper.fatal(field)
      field.getType() match {
        case t if t == classOf[Class[_]] =>
          val configClass = field.get(null).asInstanceOf[Class[_]]
          subConfigurators += new Configurator(config, configClass)
        case t =>
          val valType = ConfigValueType.forClass(t)
          if (valType != null) values += new ConfigValue(field, valType)
      }
    }
  }

  def synch() = {
    synchWithoutSave()
    if (config.hasChanged()) config.save()
  }

  def synchWithoutSave(): Unit = {
    for (value <- values) value.load(config)
    for (configurator <- subConfigurators) configurator.synchWithoutSave()
  }

  def getConfig() = config

  object ConfigValueType extends Enumeration {
    type ConfigValueType = Value
    val Boolean, Integer, Double, String, Block = Value

    def forClass(clazz: Class[_]) = {
      clazz match {
        case t if PrimitiveUtil.BOOLEAN == clazz => Boolean
        case t if PrimitiveUtil.INTEGER == clazz => Integer
        case t if PrimitiveUtil.DOUBLE == clazz => Double
        case t if clazz == classOf[String] => String
        case t if clazz == classOf[Block] => Block
        case _ => null
      }
    }
  }
  import ConfigValueType._

  class ConfigValue(field: Field, t: ConfigValueType) {
    val default = field.get(null)
    val fieldName = field.getName()
    private val splitName = fieldName.split("_")
    val name = splitName(splitName.length - 1)
    val group = if (splitName.length > 1) splitName(0) else Configuration.CATEGORY_GENERAL

    def set(obj: Any) = field.set(null, obj);
    def load(config: Configuration) = t match {
      case Boolean => set(config.get(group, name, default.asInstanceOf[Boolean]).getBoolean(default.asInstanceOf[Boolean]));
      case Integer => set(config.get(group, name, default.asInstanceOf[Int]).getInt());
      case Double => set(config.get(group, name, default.asInstanceOf[Double]).getDouble(default.asInstanceOf[Double]));
      case String => set(config.get(group, name, default.asInstanceOf[String]).getString());
      case ConfigValueType.Block =>
        val defaultBlockName = default.asInstanceOf[Block].getUnlocalizedName();
        val configProperty = config.get(group, name, defaultBlockName);
        var loadedBlock = UnlocalizedNameUtil.getBlockByUnlocalizedName(configProperty.getString());
        if (loadedBlock == null) {
          loadedBlock = default.asInstanceOf[Block];
          configProperty.set(defaultBlockName);
        }
        set(loadedBlock);
    }
  }
}