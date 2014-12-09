package com.dafttech.minechanics.client.renderer.model.gl;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.util.Color;

public class GLColor extends GLAction {
    public Color color;

    public GLColor(Color color) {
        this.color = color;
    }

    @Override
    public void apply() {
        if (color != null) Tessellator.instance.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    @Override
    public void colorize(Color color) {
        if (this.color == null) this.color = color;
    }
}
