package org.lolhens.minechanics.client.config.gui.element;

import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

public abstract class ListEntryBase extends net.minecraftforge.fml.client.config.GuiConfigEntries.ListEntryBase {
    public ListEntryBase(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement);
    }

    @Override
    public Object getCurrentValue() {
        return getCurrentValue2();
    }

    public abstract Object getCurrentValue2();
}