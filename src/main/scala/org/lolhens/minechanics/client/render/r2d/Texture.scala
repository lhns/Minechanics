package org.lolhens.minechanics.client.render.r2d

import net.minecraft.client.renderer.Tessellator

/**
 * Created by LolHens on 18.09.2014.
 */
class Texture(file: TextureFile, minU: Float, minV: Float, deltaU: Float, deltaV: Float) {
  def draw(tessellator: Tessellator, x: Float, y: Float, z: Float, texW: Float, texH: Float, areaW: Float, areaH: Float) {
    file.bind
    tessellator.startDrawingQuads

    for (minY <- 0f until areaH by texH; minX <- 0f until areaW by texW) {
      val (maxX, maxY) = (minX + texW, minY + texH)

      val maxU = minU + Math.min(deltaU, (areaW - minX) / texW * deltaU)
      val maxV = minV + Math.min(deltaV, (areaH - minY) / texH * deltaV)

      tessellator.addVertexWithUV(minX + x, maxY + y, z, minU, maxV)
      tessellator.addVertexWithUV(maxX + x, maxY + y, z, maxU, maxV)
      tessellator.addVertexWithUV(maxX + x, minY + y, z, maxU, minV)
      tessellator.addVertexWithUV(minX + x, minY + y, z, minU, minV)
    }

    tessellator.draw()
  }
}