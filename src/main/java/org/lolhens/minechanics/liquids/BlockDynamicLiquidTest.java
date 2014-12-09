package org.lolhens.minechanics.liquids;

/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://www.kpdus.com/jad.html
//Decompiler options: packimports(3) radix(10) lradix(10) 
//Source File Name:   BlockDynamicLiquid.java

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.lolhens.minechanics.core.event.Events;

import com.dafttech.eventmanager.EventType;

//Referenced classes of package net.minecraft.block:
//         BlockLiquid, Block

public class BlockDynamicLiquidTest extends BlockLiquid {
    protected BlockDynamicLiquidTest(Material p_i45403_1_) {
        super(p_i45403_1_);
        field_149814_b = new boolean[4];
        field_149816_M = new int[4];
    }

    // SET STATIC
    private void func_149811_n(World p_149811_1_, int p_149811_2_, int p_149811_3_, int p_149811_4_) {
        int i = p_149811_1_.getBlockMetadata(p_149811_2_, p_149811_3_, p_149811_4_);
        p_149811_1_.setBlock(p_149811_2_, p_149811_3_, p_149811_4_, Block.getBlockById(Block.getIdFromBlock(this) + 1), i, 2);
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        // GET LIQUID HEIGHT
        int l = func_149804_e(world, i, j, k);
        byte byte0 = 1;
        if (blockMaterial == Material.lava && !world.provider.isHellWorld) byte0 = 2;
        boolean flag = true;
        int i1 = tickRate(world);
        /*
         * mv.visitLineNumber(41, l5); mv.visitVarInsn(ALOAD, 1);
         * mv.visitVarInsn(ILOAD, 2); mv.visitVarInsn(ILOAD, 3);
         * mv.visitVarInsn(ILOAD, 4); mv.visitVarInsn(ALOAD, 0);
         * mv.visitMethodInsn(INVOKESTATIC,
         * "org/lolhens/minechanics/liquids/Liquid", "flow",
         * "(Lnet/minecraft/world/World;IIILorg/lolhens/minechanics/liquids/BlockDynamicLiquid;)Z"
         * , false); Label l6 = new Label(); mv.visitJumpInsn(IFNE, l6); Label
         * l7 = new Label(); mv.visitLabel(l7); mv.visitLineNumber(43, l7);
         * mv.visitVarInsn(ILOAD, 6); Label l8 = new Label();
         * mv.visitJumpInsn(IFLE, l8); Label l9 = new Label();
         * mv.visitLabel(l9);
         */
        if (Events.eventManager.callSync(EventType.getByName("water"), world, i, j, k, this).isCancelled()) return;
        if (l > 0) {
            int j1 = -100;
            liquidSources = 0;
            // GET HIGHEST LIQUID
            j1 = func_149810_a(world, i - 1, j, k, j1);
            j1 = func_149810_a(world, i + 1, j, k, j1);
            j1 = func_149810_a(world, i, j, k - 1, j1);
            j1 = func_149810_a(world, i, j, k + 1, j1);
            int k1 = j1 + byte0;
            if (k1 >= 8 || j1 < 0) k1 = -1;
            // GET LIQUID HEIGHT
            if (func_149804_e(world, i, j + 1, k) >= 0) {
                // GET LIQUID HEIGHT
                int i2 = func_149804_e(world, i, j + 1, k);
                if (i2 >= 8)
                    k1 = i2;
                else
                    k1 = i2 + 8;
            }
            // MAKE NEW SOURCE
            if (liquidSources >= 2 && blockMaterial == Material.water)
                if (world.getBlock(i, j - 1, k).getMaterial().isSolid())
                    k1 = 0;
                else if (world.getBlock(i, j - 1, k).getMaterial() == blockMaterial && world.getBlockMetadata(i, j - 1, k) == 0)
                    k1 = 0;
            if (blockMaterial == Material.lava && l < 8 && k1 < 8 && k1 > l && random.nextInt(4) != 0) i1 *= 4;
            if (k1 == l) {
                if (flag) func_149811_n(world, i, j, k);
            } else {
                l = k1;
                if (l < 0) {
                    world.setBlockToAir(i, j, k);
                } else {
                    world.setBlockMetadataWithNotify(i, j, k, l, 2);
                    world.scheduleBlockUpdate(i, j, k, this, i1);
                    world.notifyBlocksOfNeighborChange(i, j, k, this);
                }
            }
        } else {
            func_149811_n(world, i, j, k);
        }
        if (func_149809_q(world, i, j - 1, k)) {
            if (blockMaterial == Material.lava && world.getBlock(i, j - 1, k).getMaterial() == Material.water) {
                world.setBlock(i, j - 1, k, Blocks.stone);
                func_149799_m(world, i, j - 1, k);
                return;
            }
            if (l >= 8)
                func_149813_h(world, i, j - 1, k, l);
            else
                func_149813_h(world, i, j - 1, k, l + 8);
        } else if (l >= 0 && (l == 0 || func_149807_p(world, i, j - 1, k))) {
            boolean aflag[] = func_149808_o(world, i, j, k);
            int l1 = l + byte0;
            if (l >= 8) l1 = 1;
            if (l1 >= 8) return;
            if (aflag[0]) func_149813_h(world, i - 1, j, k, l1);
            if (aflag[1]) func_149813_h(world, i + 1, j, k, l1);
            if (aflag[2]) func_149813_h(world, i, j, k - 1, l1);
            if (aflag[3]) func_149813_h(world, i, j, k + 1, l1);
        }
    }

    private void func_149813_h(World world, int x, int y, int z, int p_149813_5_) {
        if (func_149809_q(world, x, y, z)) {
            Block block = world.getBlock(x, y, z);
            if (blockMaterial == Material.lava)
                func_149799_m(world, x, y, z);
            else
                block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, this, p_149813_5_, 3);
        }
    }

    private int func_149812_c(World world, int x, int y, int z, int p_149812_5_, int p_149812_6_) {
        int i = 1000;
        for (int j = 0; j < 4; j++) {
            if (j == 0 && p_149812_6_ == 1 || j == 1 && p_149812_6_ == 0 || j == 2 && p_149812_6_ == 3 || j == 3
                    && p_149812_6_ == 2) continue;
            int k = x;
            int l = y;
            int i1 = z;
            if (j == 0) k--;
            if (j == 1) k++;
            if (j == 2) i1--;
            if (j == 3) i1++;
            if (func_149807_p(world, k, l, i1) || world.getBlock(k, l, i1).getMaterial() == blockMaterial
                    && world.getBlockMetadata(k, l, i1) == 0) continue;
            if (func_149807_p(world, k, l - 1, i1)) {
                if (p_149812_5_ >= 4) continue;
                int j1 = func_149812_c(world, k, l, i1, p_149812_5_ + 1, j);
                if (j1 < i) i = j1;
            } else {
                return p_149812_5_;
            }
        }

        return i;
    }

    private boolean[] func_149808_o(World p_149808_1_, int p_149808_2_, int p_149808_3_, int p_149808_4_) {
        for (int i = 0; i < 4; i++) {
            field_149816_M[i] = 1000;
            int k = p_149808_2_;
            int j1 = p_149808_3_;
            int k1 = p_149808_4_;
            if (i == 0) k--;
            if (i == 1) k++;
            if (i == 2) k1--;
            if (i == 3) k1++;
            if (func_149807_p(p_149808_1_, k, j1, k1) || p_149808_1_.getBlock(k, j1, k1).getMaterial() == blockMaterial // !!!!!!!!!!!!!
                    && p_149808_1_.getBlockMetadata(k, j1, k1) == 0) continue;
            if (func_149807_p(p_149808_1_, k, j1 - 1, k1))
                field_149816_M[i] = func_149812_c(p_149808_1_, k, j1, k1, 1, i);
            else
                field_149816_M[i] = 0;
        }

        int j = field_149816_M[0];
        for (int l = 1; l < 4; l++)
            if (field_149816_M[l] < j) j = field_149816_M[l];

        for (int i1 = 0; i1 < 4; i1++)
            field_149814_b[i1] = field_149816_M[i1] == j;

        return field_149814_b;
    }

    // IS WATER RESISTANT
    private boolean func_149807_p(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block == Blocks.wooden_door || block == Blocks.iron_door || block == Blocks.standing_sign || block == Blocks.ladder
                || block == Blocks.reeds) return true;
        if (block.getMaterial() == Material.portal && world.getBlockMetadata(x, y, z) == 0)
            return true;
        else
            return block.getMaterial().blocksMovement();
    }

    // GET HIGHTEST LIQUID BLOCK
    protected int func_149810_a(World world, int x, int y, int z, int liquidHeight) {
        // GET LIQUID HEIGHT
        int foundHeight = func_149804_e(world, x, y, z);
        // NOT A LIQUID RETURN BACK THE GIVEN HEIGHT
        if (foundHeight < 0) return liquidHeight;
        // SOURCE ADD TO THE UNKNOWN FIELD
        if (foundHeight == 0) liquidSources++;
        // FALLING
        if (foundHeight >= 8) foundHeight = 0;
        // CHECK FOR THE HIGHER HEIGHT
        return liquidHeight >= 0 && foundHeight >= liquidHeight ? liquidHeight : foundHeight;
    }

    // Blocks fluid
    private boolean func_149809_q(World world, int x, int y, int z) {
        Material material = world.getBlock(x, y, z).getMaterial();
        if (material == blockMaterial) return false;
        if (material == Material.lava)
            return false;
        else
            return !func_149807_p(world, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
        if (world.getBlock(i, j, k) == this) world.scheduleBlockUpdate(i, j, k, this, tickRate(world));
    }

    @Override
    public boolean func_149698_L() {
        return true;
    }

    int liquidSources;
    boolean field_149814_b[];
    int field_149816_M[];
}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * C:\Users\Pierre\git\Minechanics2\build\dirtyArtifacts\forgeBin
 * -1.7.2-10.12.0.1024.jar Total time: 41 ms Jad reported messages/errors: Exit
 * status: 0 Caught exceptions:
 */