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
import reflect.runtime.universe._
import scala.reflect.ClassTag

class Configurator[T: TypeTag: ClassTag](configuration: Configuration, config: T) {
  def this(event: FMLPreInitializationEvent, config: T) = {
    this(new Configuration(event.getSuggestedConfigurationFile()), config)
  }

  val values = new MutableList[ConfigValue]()
  val subConfigurators = new MutableList[Configurator[_]]()

  private val mirror = runtimeMirror(getClass.getClassLoader)

  for (field <- typeOf[T].members.collect { case m: MethodSymbol if m.isGetter => m }) {
    LogHelper.fatal(field)
    val fieldMirror = mirror.reflect(config).reflectField(field)

    typeOf[T] match {
      //case t if t =:= typeOf[Class[_]] => subConfigurators += new Configurator(config, fieldMirror.get.asInstanceOf[Class[_]])
      case _ =>
        val configValue = new ConfigValue(fieldMirror)
        if (configValue.isValid()) values += configValue
    }
  }

  def synch() = {
    synchWithoutSave()
    if (configuration.hasChanged()) configuration.save()
  }

  def synchWithoutSave(): Unit = {
    for (value <- values) value.load(configuration)
    for (configurator <- subConfigurators) configurator.synchWithoutSave()
  }

  def getConfig() = configuration
}