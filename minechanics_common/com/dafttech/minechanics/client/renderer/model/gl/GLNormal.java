package com.dafttech.minechanics.client.renderer.model.gl;

import net.minecraft.client.renderer.Tessellator;

import com.dafttech.minechanics.util.Vector3f;

public class GLNormal extends GLVector {

    public GLNormal(Vector3f pos) {
        super(pos);
        resize = false;
        translate = false;
    }

    public GLNormal(float x, float y, float z) {
        super(x, y, z);
        resize = false;
        translate = false;
    }

    @Override
    public void apply() {
        Tessellator.instance.setNormal(pos.x, pos.y, pos.z);
    }

    @Override
    public void rotate(Vector3f rotation, Vector3f origin) {
        if (rotate) pos.rotate(rotation, new Vector3f());
    }
}
