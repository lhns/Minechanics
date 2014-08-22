package org.lolhens.minechanics.common.util

import cpw.mods.fml.common.FMLLog
import org.apache.logging.log4j.Level;
import org.lolhens.minechanics.Minechanics

object LogHelper {
  def log(logLevel: Level, obj: Object) = FMLLog.log(Minechanics.Name, logLevel, String.valueOf(obj))
  def all(obj: Object) = log(Level.ALL, obj)
  def debug(obj: Object) = log(Level.DEBUG, obj)
  def error(obj: Object) = log(Level.ERROR, obj)
  def fatal(obj: Object) = log(Level.FATAL, obj)
  def info(obj: Object) = log(Level.INFO, obj)
  def off(obj: Object) = log(Level.OFF, obj)
  def trace(obj: Object) = log(Level.TRACE, obj)
  def warn(obj: Object) = log(Level.WARN, obj)
}