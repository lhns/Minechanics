package org.lolhens.minechanics.core.client.render;

import net.minecraft.client.renderer.Tessellator;

public class Texture {
    private TextureFile file;
    private float u, v, deltaU, deltaV;

    public Texture(TextureFile file, float u, float v, float deltaU, float deltaV) {
        this.file = file;
        this.u = u;
        this.v = v;
        this.deltaU = deltaU;
        this.deltaV = deltaV;
    }

    public void draw(Tessellator tessellator, float x, float y, float z, float texW, float texH, float areaW, float areaH) {
        file.bind();
        tessellator.startDrawingQuads();
        float maxX, maxY, minU = u, minV = v, maxU, maxV, localDeltaU, localDeltaV;
        for (float minY = 0; minY < areaH; minY += texH) {
            for (float minX = 0; minX < areaW; minX += texW) {
                maxX = minX += texW;
                maxY = minY += texH;
                localDeltaU = deltaU;
                localDeltaV = deltaV;
                if (maxX > areaW) localDeltaU *= (maxX - areaW) / texW;
                if (maxY > areaH) localDeltaV *= (maxY - areaH) / texH;
                maxU = u + localDeltaU;
                maxV = u + localDeltaV;
                tessellator.addVertexWithUV(minX, maxY, z, minU, maxV);
                tessellator.addVertexWithUV(maxX, maxY, z, maxU, maxV);
                tessellator.addVertexWithUV(maxX, minY, z, maxU, minV);
                tessellator.addVertexWithUV(minX, minY, z, minU, minV);
            }
        }
        tessellator.draw();
    }

    public TextureFile getTextureFile() {
        return file;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public float getDeltaU() {
        return deltaU;
    }

    public float getDeltaV() {
        return deltaV;
    }
}
