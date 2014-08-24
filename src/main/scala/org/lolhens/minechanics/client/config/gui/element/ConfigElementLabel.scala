package org.lolhens.minechanics.client.config.gui.element

import cpw.mods.fml.client.config.IConfigElement
import net.minecraftforge.common.config.ConfigCategory
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry

class ConfigElementLabel(ctgy: ConfigCategory, value: String) extends IConfigElement[Object] {
  override def isProperty() = false
  override def getConfigEntryClass() = classOf[ConfigEntryLabel]
  override def getArrayEntryClass() = null
  override def getName() = value
  override def getQualifiedName() = null
  override def getLanguageKey() = ctgy.getLanguagekey()
  override def getComment() = null
  override def getChildElements() = null
  override def getType() = null
  override def isList() = false
  override def isListLengthFixed() = true
  override def getMaxListLength() = 0
  override def isDefault() = true
  override def getDefault() = null
  override def getDefaults() = null
  override def setToDefault() = {}
  override def requiresWorldRestart() = false
  override def showInGui() = true
  override def requiresMcRestart() = false
  override def get() = null
  override def getList() = null
  override def getValidValues() = null
  override def getMinValue() = null
  override def getMaxValue() = null
  override def getValidationPattern() = null
  override def set(arg0: Object) {}
  override def set(arg0: Array[Object]) {}
}