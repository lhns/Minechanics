package com.dafttech.minechanics.client.renderer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.dafttech.minechanics.data.ModConfig;
import com.dafttech.minechanics.event.InfoBoundingBox;
import com.dafttech.minechanics.interfaces.ISmoothWaterTransition;
import com.dafttech.minechanics.util.Vec3data;

public class BlockRenderer {
    public static List<Block> smoothWaterTransitions = new ArrayList<Block>();
    public static final int BOX = 0, CROSS = 1;
    private RenderBlocks renderer;
    private Tessellator tessellator;
    private Block block;
    private IBlockAccess world;
    private float sX, sY, sZ, sA, sB, sC;
    private double x, y, z;
    private int texId;
    private String texFile = null;
    private boolean inventory = true, boundingBox = false;

    public BlockRenderer(RenderBlocks renderer, Block block, float x, float y, float z, float a, float b, float c, int texId, String texFile) {
        this.renderer = renderer;
        tessellator = Tessellator.instance;
        this.block = block;
        sX = x;
        sY = y;
        sZ = z;
        sA = a;
        sB = b;
        sC = c;
        this.texId = texId;
        this.texFile = texFile;
    }

    public BlockRenderer(RenderBlocks renderer, Block block, float x, float y, float z, float a, float b, float c, int texId) {
        this.renderer = renderer;
        tessellator = Tessellator.instance;
        this.block = block;
        sX = x;
        sY = y;
        sZ = z;
        sA = a;
        sB = b;
        sC = c;
        this.texId = texId;
    }

    public void render(int type) {
        setupRenderer();
        if (type == BOX) {
            if (inventory) {
                drawBlock();
            } else {
                renderBlock();
            }
        } else if (type == CROSS) {
            if (inventory) {
                drawCrossedSquares();
            } else {
                renderCrossedSquares();
            }
        }
        resetRenderer();
    }

    public BlockRenderer setInfoBoundingBox(InfoBoundingBox info) {
        // renderer.updateCustomBlockBounds(block);
        double xTrans = info.player.lastTickPosX + (info.player.posX - info.player.lastTickPosX) * info.parTicks;
        double yTrans = info.player.lastTickPosY + (info.player.posY - info.player.lastTickPosY) * info.parTicks;
        double zTrans = info.player.lastTickPosZ + (info.player.posZ - info.player.lastTickPosZ) * info.parTicks;
        tessellator.setTranslation(-xTrans, -yTrans, -zTrans);
        boundingBox = true;
        return this;
    }

    public BlockRenderer at(IBlockAccess world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        inventory = false;
        return this;
    }

    private void setupRenderer() {
        // renderer.setCustomBlockBounds(sX, sY, sZ, sA, sB, sC);
        // renderer.overrideBlockTexture = texId;
        if (texFile != null) {
            // BlockManager.autoregisterTexture(texFile);
            // ForgeHooksClient.bindTexture(texFile, 0);
        } else if (boundingBox) {
            // ForgeHooksClient.bindTexture(block.getTextureFile(), 0);
        }
        if (boundingBox) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
        // GL11.glRotatef(45, 0, 1, 0);
        if (!tessellator.isDrawing && (inventory || boundingBox)) {
            tessellator.startDrawingQuads();
        }
    }

    private void resetRenderer() {
        /*
         * if (tessellator.isDrawing && (inventory || boundingBox)) {
         * tessellator.draw();//startDrawingQuads(); }
         */
        if (inventory || boundingBox) {
            if (tessellator.isDrawing) tessellator.draw();
        } else {
            if (!tessellator.isDrawing) tessellator.startDrawingQuads();
        }
        // GL11.glRotatef(-45, 0, 1, 0);
        if (boundingBox) {
            tessellator.setTranslation(0, 0, 0);
            GL11.glDisable(GL11.GL_BLEND);
            boundingBox = false;
        }
        if (texFile != null || boundingBox) {
            // ForgeHooksClient.unbindTexture();
        }
        // renderer.overrideBlockTexture = -1;
        // renderer.resetCustomBlockBounds();
    }

    public static int getTextureIdAt(int x, int y) {
        return y * 16 + x;
    }

    public static boolean shouldRenderSmoothWaterTransition(IBlockAccess world, int x, int y, int z) {
        if (!ModConfig.underwaterRenderer) return false;
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        return block != null
                && (!(block instanceof ISmoothWaterTransition) && BlockRenderer.smoothWaterTransitions.contains(block) || block instanceof ISmoothWaterTransition
                        && ((ISmoothWaterTransition) block).isSmoothWaterTransition(world, x, y, z)) && BlockRenderer.cantSeeInBlock(world, x, y, z);
    }

    public static boolean blockSightTo(IBlockAccess world, int x, int y, int z) {
        if (Block.blocksList[world.getBlockId(x, y, z)] != null) {
            // RenderBlocks renderer = new RenderBlocks(world);
            // new BlockRenderer(renderer, Block.blocksList[world.getBlockId(x,
            // y, z)], 0.999F, 0.999F, 0.999F, 0.001F, 0.001F, 0.001F, 11,
            // Minechanics.proxy.sprites).at(world, x, y,
            // z).render(BlockRenderer.BOX);
        }
        return true;
    }

    public static boolean cantSeeInBlock(IBlockAccess world, int x, int y, int z) {
        return cantSeeInBlock(world, x, y, z, new ArrayList<Vec3data>(), 0);
    }

    private static boolean cantSeeInBlock(IBlockAccess world, int x, int y, int z, List<Vec3data> edited, int rec) {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        int editedIndex = edited.indexOf(new Vec3data(x, y, z));
        if (block == null) return false;
        if (block.isOpaqueCube() || rec > 4) return true;
        if (editedIndex > -1) return (Boolean) edited.get(editedIndex).data[0];
        Vec3data myVec = new Vec3data(x, y, z, true);
        edited.add(myVec);
        int ret = 0;
        if (isBlockingSight(world, x, y + 1, z, false) || cantSeeInBlock(world, x, y + 1, z, edited, rec + 1)) ret++;
        if (ret != 1) {
            myVec.data[0] = false;
            return false;
        }
        if (isBlockingSight(world, x + 1, y, z, true) || cantSeeInBlock(world, x + 1, y, z, edited, rec + 1)) ret++;
        if (ret != 2) {
            myVec.data[0] = false;
            return false;
        }
        if (isBlockingSight(world, x - 1, y, z, true) || cantSeeInBlock(world, x - 1, y, z, edited, rec + 1)) ret++;
        if (ret != 3) {
            myVec.data[0] = false;
            return false;
        }
        if (isBlockingSight(world, x, y, z + 1, true) || cantSeeInBlock(world, x, y, z + 1, edited, rec + 1)) ret++;
        if (ret != 4) {
            myVec.data[0] = false;
            return false;
        }
        if (isBlockingSight(world, x, y, z - 1, true) || cantSeeInBlock(world, x, y, z - 1, edited, rec + 1)) ret++;
        if (ret != 5) {
            myVec.data[0] = false;
            return false;
        }
        return true;
    }

    public static boolean isBlockingSight(IBlockAccess world, int x, int y, int z) {
        return isBlockingSight(world, x, y, z, false);
    }

    private static boolean isBlockingSight(IBlockAccess world, int x, int y, int z, boolean topLiqCheck) {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        Block upperblock = Block.blocksList[world.getBlockId(x, y + 1, z)];
        boolean topLiq = !topLiqCheck || upperblock != null && upperblock instanceof BlockFluid;
        if (block != null && (block.isOpaqueCube() || block instanceof BlockFluid && topLiq)) return true;
        return false;
    }

    // ======================[ RENDER METHODS ]=================================
    private boolean renderCrossedSquares() {
        // System.out.println(block.getMixedBrightnessForBlock(world, (int) x,
        // (int) y, (int) z));
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int) x, (int) y, (int) z));
        int colorMultiplier = block.colorMultiplier(world, (int) x, (int) y, (int) z);
        float r = (colorMultiplier >> 16 & 255) / 255.0F;
        float g = (colorMultiplier >> 8 & 255) / 255.0F;
        float b = (colorMultiplier & 255) / 255.0F;
        if (EntityRenderer.anaglyphEnable) {
            float var11 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float var12 = (r * 30.0F + g * 70.0F) / 100.0F;
            float var13 = (r * 30.0F + b * 70.0F) / 100.0F;
            r = var11;
            g = var12;
            b = var13;
        }
        tessellator.setColorOpaque_F(r, g, b);
        drawCrossedSquares();
        return true;
    }

    private void drawCrossedSquares() {
        int tex1 = (texId & 15) << 4;
        int tex2 = texId & 240;
        double texX1 = tex1 / 256.0F;
        double texX2 = (tex1 + 15.99F) / 256.0F;
        double texY1 = tex2 / 256.0F;
        double texY2 = (tex2 + 15.99F) / 256.0F;
        // if (inventory)
        // tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + sX, y + sB, z + sZ, texX1, texY1);
        tessellator.addVertexWithUV(x + sX, y + sY, z + sZ, texX1, texY2);
        tessellator.addVertexWithUV(x + sA, y + sY, z + sC, texX2, texY2);
        tessellator.addVertexWithUV(x + sA, y + sB, z + sC, texX2, texY1);
        tessellator.addVertexWithUV(x + sA, y + sB, z + sC, texX1, texY1);
        tessellator.addVertexWithUV(x + sA, y + sY, z + sC, texX1, texY2);
        tessellator.addVertexWithUV(x + sX, y + sY, z + sZ, texX2, texY2);
        tessellator.addVertexWithUV(x + sX, y + sB, z + sZ, texX2, texY1);
        tessellator.addVertexWithUV(x + sX, y + sB, z + sC, texX1, texY1);
        tessellator.addVertexWithUV(x + sX, y + sY, z + sC, texX1, texY2);
        tessellator.addVertexWithUV(x + sA, y + sY, z + sZ, texX2, texY2);
        tessellator.addVertexWithUV(x + sA, y + sB, z + sZ, texX2, texY1);
        tessellator.addVertexWithUV(x + sA, y + sB, z + sZ, texX1, texY1);
        tessellator.addVertexWithUV(x + sA, y + sY, z + sZ, texX1, texY2);
        tessellator.addVertexWithUV(x + sX, y + sY, z + sC, texX2, texY2);
        tessellator.addVertexWithUV(x + sX, y + sB, z + sC, texX2, texY1);
        if (inventory) tessellator.draw();
    }

    private void renderBlock() {
        renderer.renderStandardBlock(block, (int) x, (int) y, (int) z);
    }

    private void drawBlock() {
        int standardMeta = 0;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, standardMeta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, standardMeta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, standardMeta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, standardMeta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, standardMeta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, standardMeta));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
}
