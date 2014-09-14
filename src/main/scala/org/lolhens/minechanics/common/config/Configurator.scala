package org.lolhens.minechanics.common.config

import net.minecraftforge.common.config.Configuration
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import scala.collection.mutable.MutableList
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import org.lolhens.minechanics.Minechanics
import net.minecraft.block.Block
import org.lolhens.minechanics.common.util.LogHelper
import org.lolhens.minechanics.common.util.UnlocalizedNameUtil
import reflect.runtime.universe._
import scala.reflect.ClassTag

class Configurator[T: TypeTag: ClassTag](configuration: Configuration, config: T) {
  def this(event: FMLPreInitializationEvent, config: T) = {
    this(new Configuration(event.getSuggestedConfigurationFile), config)
  }

  val values = new MutableList[ConfigValue]()
  val subConfigurators = new MutableList[Configurator[_]]()

  private val mirror = runtimeMirror(getClass.getClassLoader)

  for (field <- typeOf[T].members.collect { case m: MethodSymbol if m.isGetter => m }) {
    val fieldMirror = mirror.reflect(config).reflectField(field)

    fieldMirror.symbol.typeSignature match {
      case t if (ConfigValue.isValidType(t)) => values += new ConfigValue(fieldMirror)
      case _ =>
    }
  }

  def synch() = {
    synchWithoutSave()
    if (configuration.hasChanged) configuration.save
  }

  def synchWithoutSave(): Unit = {
    for (value <- values) value.load(configuration)
    for (configurator <- subConfigurators) configurator.synchWithoutSave
  }

  def getConfig() = configuration
}