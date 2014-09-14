package org.lolhens.minechanics.core.util

import cpw.mods.fml.common.FMLLog
import org.apache.logging.log4j.Level;
import org.lolhens.minechanics.Minechanics

object LogHelper {
  def log(logLevel: Level, obj: Any) = FMLLog.log(Minechanics.Name, logLevel, String.valueOf(obj))
  def all(obj: Any) = log(Level.ALL, obj)
  def debug(obj: Any) = log(Level.DEBUG, obj)
  def error(obj: Any) = log(Level.ERROR, obj)
  def fatal(obj: Any) = log(Level.FATAL, obj)
  def info(obj: Any) = log(Level.INFO, obj)
  def off(obj: Any) = log(Level.OFF, obj)
  def trace(obj: Any) = log(Level.TRACE, obj)
  def warn(obj: Any) = log(Level.WARN, obj)
}