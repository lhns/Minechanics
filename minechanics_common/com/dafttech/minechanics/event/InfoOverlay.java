package com.dafttech.minechanics.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class InfoOverlay {
    public Minecraft mc;
    public Tessellator tessellator;
    public FontRenderer fontRenderer;
    public ScaledResolution scaledResolution;
    public int scaledWidth;
    public int scaledHeight;
    public World world;
    public EntityPlayer player;

    public InfoOverlay(Minecraft mc, ScaledResolution scaledResolution) {
        this.mc = mc;
        tessellator = Tessellator.instance;
        this.scaledResolution = scaledResolution;
        fontRenderer = mc.fontRenderer;
        scaledWidth = scaledResolution.getScaledWidth();
        scaledHeight = scaledResolution.getScaledHeight();
        world = mc.theWorld;
        player = mc.thePlayer;
    }
}
