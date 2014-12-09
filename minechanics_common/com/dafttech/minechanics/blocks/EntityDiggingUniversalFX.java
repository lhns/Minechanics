package com.dafttech.minechanics.blocks;

import java.util.Random;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import com.dafttech.minechanics.blocks.BlockMinechanics.RenderMode;
import com.dafttech.minechanics.client.renderer.model.Model;
import com.dafttech.minechanics.util.Vector3f;

public class EntityDiggingUniversalFX extends EntityFX {
    private static Random rnd = new Random();

    private BlockUniversal blockInstance;
    private float rotX = 0, rotY = 0, rotZ = 0;
    private Model model;

    public EntityDiggingUniversalFX(World world, int x, int y, int z, int metadata, double size, BlockUniversal block) {
        super(world, x + rnd.nextFloat(), y + rnd.nextFloat(), z + rnd.nextFloat(), rnd.nextFloat() - 0.5f, rnd.nextFloat() - 0.5f,
                rnd.nextFloat() - 0.5f);
        rotX = rnd.nextFloat() - 0.5f;
        rotY = rnd.nextFloat() - 0.5f;
        rotZ = rnd.nextFloat() - 0.5f;
        blockInstance = block;
        particleGravity = block.blockParticleGravity;
        particleRed = particleGreen = particleBlue = 0.6F;
        particleScale = (float) size;
        model = new Model(blockInstance.blockMcncs.getInstance(null).getModel(RenderMode.INVENTORY, 0));
        model.setBrightness(worldObj, x, y, z);
        model.setSize(new Vector3f(particleScale));
    }

    public EntityDiggingUniversalFX applyColourMultiplier(int par1, int par2, int par3) {
        return this;
    }

    public EntityDiggingUniversalFX applyRenderColor(int par1) {
        return this;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
        if (blockInstance instanceof BlockUniversal) {
            if (model != null) {
                float renderX = (float) (prevPosX + (posX - prevPosX) * par2 - interpPosX);
                float renderY = (float) (prevPosY + (posY - prevPosY) * par2 - interpPosY);
                float renderZ = (float) (prevPosZ + (posZ - prevPosZ) * par2 - interpPosZ);
                if (!onGround) model.rotate(new Vector3f(rotX, rotY, rotZ).mul(0.1f));
                new Model(model).render(new Vector3f(renderX, renderY, renderZ));
            }
        }
    }
}
