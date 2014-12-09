package org.lolhens.minechanics.core.client.render;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class TextureFile {
    private ResourceLocation resourceLocation;

    public TextureFile(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public void bind() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(resourceLocation);
    }
}
