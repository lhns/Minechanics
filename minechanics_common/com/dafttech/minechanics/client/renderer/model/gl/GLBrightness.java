package com.dafttech.minechanics.client.renderer.model.gl;

import net.minecraft.client.renderer.Tessellator;

public class GLBrightness extends GLAction {
    public Integer brightness;

    public GLBrightness() {
    }

    public GLBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    @Override
    public void apply() {
        if (brightness != null) Tessellator.instance.setBrightness(brightness);
    }

    @Override
    public void illuminate(Integer brightness) {
        if (this.brightness == null) this.brightness = brightness;
    }
}
