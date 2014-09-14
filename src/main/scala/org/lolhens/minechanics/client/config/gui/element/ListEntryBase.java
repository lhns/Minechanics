package org.lolhens.minechanics.client.config.gui.element;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;

public abstract class ListEntryBase extends cpw.mods.fml.client.config.GuiConfigEntries.ListEntryBase {
    public ListEntryBase(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<?> configElement) {
        super(owningScreen, owningEntryList, configElement);
    }

    @Override
    public Object getCurrentValue() {
        return getCurrentValue2();
    }

    public abstract Object getCurrentValue2();
}