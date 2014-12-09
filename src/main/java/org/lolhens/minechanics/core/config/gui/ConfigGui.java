package org.lolhens.minechanics.core.config.gui;

import static org.lolhens.minechanics.core.reference.Reference.MOD_ID;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import org.lolhens.minechanics.Minechanics;
import org.lolhens.minechanics.core.config.Configurator;
import org.lolhens.minechanics.core.config.gui.label.LabelConfigElement;
import org.lolhens.minechanics.core.event.Events;
import org.lolhens.minechanics.core.reference.Reference;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigGui extends GuiConfig {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ConfigGui(GuiScreen parent) {
        super(parent, new ArrayList(), Reference.MOD_ID, false, false, Reference.MOD_NAME);
        addGuiElements(configElements, Minechanics.instance.configurator);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SuppressWarnings("rawtypes")
    public void addGuiElements(List<IConfigElement> elements, Configurator configurator) {
        Configuration config = configurator.getConfiguration();
        ConfigCategory category;
        for (String categoryName : config.getCategoryNames()) {
            category = config.getCategory(categoryName);
            elements.add(new LabelConfigElement(category, firstLetterToUpperCase(categoryName)));
            elements.addAll(new ConfigElement<Object>(category).getChildElements());
        }
    }

    private static String firstLetterToUpperCase(String string) {
        return string.length() > 1 ? string.substring(0, 1).toUpperCase() + string.substring(1) : string;
    }

    @SubscribeEvent
    public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID == MOD_ID) Events.eventManager.callSync(Events.configChanged, event);
    }
}
