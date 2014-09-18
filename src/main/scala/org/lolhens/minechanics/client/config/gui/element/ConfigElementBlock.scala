package org.lolhens.minechanics.client.config.gui.element

import net.minecraftforge.common.config.{ConfigElement, Property}

class ConfigElementBlock(prop: Property) extends ConfigElement[String](prop) {
  override def getConfigEntryClass() = classOf[ConfigEntryBlock]

  override def getName() = super.getName() match {
    case name if name.startsWith("block:") => name.replaceFirst("block:", "")
    case name => name
  }
}