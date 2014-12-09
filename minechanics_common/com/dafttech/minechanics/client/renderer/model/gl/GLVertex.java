package com.dafttech.minechanics.client.renderer.model.gl;

import net.minecraft.client.renderer.Tessellator;

import com.dafttech.minechanics.client.renderer.model.Texture;
import com.dafttech.minechanics.util.Vector3f;

public class GLVertex extends GLVector {
    public Texture tex;

    public GLVertex(Vector3f pos, Texture tex) {
        super(pos);
        this.tex = tex;
    }

    public GLVertex(float x, float y, float z, Texture tex) {
        super(x, y, z);
        this.tex = tex;
    }

    @Override
    public void apply() {
        Tessellator.instance.addVertexWithUV(pos.x, pos.y, pos.z, vertex < 2 ? tex.getMinU() : tex.getMaxU(),
                vertex % 3 == 0 ? tex.getMinV() : tex.getMaxV());
        vertex = (vertex + 1) % 4;
    }

    private static int vertex = 0;
}
