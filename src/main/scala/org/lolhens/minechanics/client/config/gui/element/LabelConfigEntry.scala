package org.lolhens.minechanics.client.config.gui.element

import cpw.mods.fml.client.config.GuiConfigEntries
import cpw.mods.fml.client.config.IConfigElement
import cpw.mods.fml.client.config.GuiConfig
import net.minecraft.util.EnumChatFormatting
import net.minecraft.client.renderer.Tessellator
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry

class LabelConfigEntry(owningScreen: GuiConfig, owningEntryList: GuiConfigEntries, configElement: IConfigElement[_]) extends ListEntryBase(owningScreen, owningEntryList, configElement) {
  override def drawEntry(slotIndex: Int, x: Int, y: Int, listWidth: Int, slotHeight: Int, tessellator: Tessellator, mouseX: Int, mouseY: Int, isSelected: Boolean): Unit = {
    if (!drawLabel) return
    val label = EnumChatFormatting.WHITE.toString() + name
    val drawX = owningScreen.entryList.labelX + (owningEntryList.scrollBarX - mc.fontRenderer.getStringWidth(label)) / 2
    val drawY = y + slotHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2
    mc.fontRenderer.drawString(label, drawX, drawY, 16777215)
  }
  override def keyTyped(c: Char, i: Int) = {}
  override def updateCursorCounter() = {}
  override def mouseClicked(i: Int, j: Int, k: Int) = {}
  override def isDefault() = true
  override def setToDefault() = {}
  override def isChanged() = false
  override def undoChanges() = {}
  override def saveConfigElement() = false
  override def getCurrentValues() = null
  override def getCurrentValue2() = null
}