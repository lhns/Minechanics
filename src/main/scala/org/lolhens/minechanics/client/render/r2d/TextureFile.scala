package org.lolhens.minechanics.client.render.r2d

import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 * Created by LolHens on 18.09.2014.
 */
class TextureFile(resourceLocation: ResourceLocation) {
  def bind = {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    FMLClientHandler.instance.getClient.getTextureManager.bindTexture(resourceLocation)
  }
}
