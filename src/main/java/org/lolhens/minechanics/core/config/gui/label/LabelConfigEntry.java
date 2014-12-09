package org.lolhens.minechanics.core.config.gui.label;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.ListEntryBase;
import cpw.mods.fml.client.config.IConfigElement;

public class LabelConfigEntry extends ListEntryBase {
    public LabelConfigEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<?> configElement) {
        super(owningScreen, owningEntryList, configElement);
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX,
            int mouseY, boolean isSelected) {
        if (drawLabel) {
            String label = EnumChatFormatting.WHITE.toString() + name;
            mc.fontRenderer.drawString(label,
                    owningScreen.entryList.labelX + (owningEntryList.scrollBarX - mc.fontRenderer.getStringWidth(label)) / 2, y
                            + slotHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
        }
    }

    @Override
    public void keyTyped(char c, int i) {
    }

    @Override
    public void updateCursorCounter() {
    }

    @Override
    public void mouseClicked(int i, int j, int k) {
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public void setToDefault() {
    }

    @Override
    public boolean isChanged() {
        return false;
    }

    @Override
    public void undoChanges() {
    }

    @Override
    public boolean saveConfigElement() {
        return false;
    }

    @Override
    public Object getCurrentValue() {
        return null;
    }

    @Override
    public Object[] getCurrentValues() {
        return null;
    }
}