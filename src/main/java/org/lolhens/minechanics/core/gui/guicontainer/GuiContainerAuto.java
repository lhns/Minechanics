package org.lolhens.minechanics.core.gui.guicontainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lolhens.minechanics.core.client.render.Texture;
import org.lolhens.minechanics.core.client.render.TextureFile;

public class GuiContainerAuto extends GuiContainer {

    public GuiContainerAuto(Container container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        new Texture(new TextureFile(new ResourceLocation("gui")), 0, 0, 1, 1).draw(Tessellator.instance, 0, 0, 0, 10, 10, 10, 10);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {

    }

}
