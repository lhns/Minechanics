package org.lolhens.minechanics.core.config.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;

public class ConfigGuiFactory implements IModGuiFactory {

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement arg0) {
        return null;
    }

    @Override
    public void initialize(Minecraft arg0) {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

}
