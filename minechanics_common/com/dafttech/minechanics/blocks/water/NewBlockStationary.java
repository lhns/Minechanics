package com.dafttech.minechanics.blocks.water;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.dafttech.minechanics.data.ModInfo;

public class NewBlockStationary extends net.minecraft.block.BlockFlowing {
    private Liquid liquid;

    public NewBlockStationary(int blockID, Material material) {
        super(blockID, material);
        liquid = Liquid.getById(blockID);
        NewBlockPistonBase.setMobilityFlag(blockID, 0, ModInfo.MOBILITY_PRESSURE);
    }

    @Override
    public int getMobilityFlag() {
        return ModInfo.MOBILITY_BREAKABLE;
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess iba, int x, int y, int z) {
        return blockMaterial != Material.lava;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
        super.onNeighborBlockChange(world, x, y, z, neighborID);
        if (world.getBlockId(x, y, z) == blockID) liquid.setMoving(world, x, y, z);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random rnd) {
        if (blockMaterial == Material.lava) {
            int randInt = rnd.nextInt(3);
            int xOff = x, yOff = y, zOff = z;
            int iteration;
            int id;

            for (iteration = 0; iteration < randInt; ++iteration) {
                xOff += rnd.nextInt(3) - 1;
                ++yOff;
                zOff += rnd.nextInt(3) - 1;
                id = world.getBlockId(xOff, yOff, zOff);

                if (id == 0) {
                    if (this.isFlammable(world, xOff - 1, yOff, zOff) || this.isFlammable(world, xOff + 1, yOff, zOff)
                            || this.isFlammable(world, xOff, yOff, zOff - 1) || this.isFlammable(world, xOff, yOff, zOff + 1)
                            || this.isFlammable(world, xOff, yOff - 1, zOff) || this.isFlammable(world, xOff, yOff + 1, zOff)) {
                        world.setBlock(xOff, yOff, zOff, Block.fire.blockID);
                        return;
                    }
                } else if (Block.blocksList[id].blockMaterial.blocksMovement()) {
                    return;
                }
            }

            if (randInt == 0) {
                iteration = xOff;
                id = zOff;

                for (int k1 = 0; k1 < 3; ++k1) {
                    xOff = iteration + rnd.nextInt(3) - 1;
                    zOff = id + rnd.nextInt(3) - 1;

                    if (world.isAirBlock(xOff, yOff + 1, zOff) && this.isFlammable(world, xOff, yOff, zOff)) {
                        world.setBlock(xOff, yOff + 1, zOff, Block.fire.blockID);
                    }
                }
            }
        }
    }

    /**
     * Checks to see if the block is flammable.
     */
    private boolean isFlammable(World world, int x, int y, int z) {
        return world.getBlockMaterial(x, y, z).getCanBurn();
    }
}
