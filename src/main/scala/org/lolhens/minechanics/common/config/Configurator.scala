package org.lolhens.minechanics.common.config

import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

import scala.collection.mutable.MutableList
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

class Configurator[T: TypeTag : ClassTag](configuration: Configuration, config: T) {
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