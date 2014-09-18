package org.lolhens.minechanics.client.config.gui.element

import cpw.mods.fml.client.config.{GuiConfig, GuiConfigEntries, IConfigElement}
import net.minecraft.client.renderer.Tessellator
import org.lolhens.minechanics.common.util.UnlocalizedNameUtil

class ConfigEntryBlock(owningScreen: GuiConfig, owningEntryList: GuiConfigEntries, configElement: IConfigElement[_])
  extends GuiConfigEntries.StringEntry(owningScreen, owningEntryList, configElement) {

  val colorInvalid = 0xDD0000

  override def drawEntry(slotIndex: Int, x: Int, y: Int, listWidth: Int, slotHeight: Int, tessellator: Tessellator, mouseX: Int, mouseY: Int, isSelected: Boolean) {
    super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected)
    textFieldValue.xPosition = this.owningEntryList.controlX + 2
    textFieldValue.yPosition = y + 1
    textFieldValue.width = this.owningEntryList.controlWidth - 4
    textFieldValue.setEnabled(enabled)
    textFieldValue.setTextColor(if (validate(textFieldValue.getText)) 14737632 else 0xDD0000)
    textFieldValue.drawTextBox
  }

  override def isChanged() = super.isChanged && validate(textFieldValue.getText)

  def validate(text: String) = UnlocalizedNameUtil.getBlockByUnlocalizedName(text) != null
}