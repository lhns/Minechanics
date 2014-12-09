package com.dafttech.minechanics.blocks;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.dafttech.minechanics.blocks.BlockMinechanics.RenderMode;

public class TileEntityRendererUniversal extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float delta) {
        TileEntityUniversal te = (TileEntityUniversal) tileentity;
        bindTexture(TextureMap.locationBlocksTexture);
        RenderHelper.disableStandardItemLighting();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef(-te.xCoord + (float) x, -te.yCoord + (float) y, -te.zCoord + (float) z);
        if (te.blockMcncs != null)
            te.blockMcncs.getNewInstance(te.blockMcncs.block, -1, tileentity.worldObj, te.xCoord, te.yCoord, te.zCoord).render(RenderMode.WORLD,
                    null, delta);
        GL11.glTranslatef(-(-te.xCoord + (float) x), -(-te.yCoord + (float) y), -(-te.zCoord + (float) z));
        RenderHelper.enableStandardItemLighting();
    }
}
