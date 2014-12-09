package com.dafttech.minechanics.blocks.water;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.dafttech.minechanics.data.ModConfig;
import com.dafttech.minechanics.data.ModInfo;

public class NewBlockFlowing extends net.minecraft.block.BlockFlowing {
    private int numAdjacentSources = 0;
    private boolean[] isOptimalFlowDirection = new boolean[4];
    private int[] flowCost = new int[4];
    private Liquid liquid;

    public NewBlockFlowing(int blockID, Material material) {
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
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random rnd) {
        int decay = getFlowDecay(world, x, y, z);
        byte b0 = 1;

        if (blockMaterial == Material.lava && !world.provider.isHellWorld) {
            b0 = 2;
        }

        boolean flag = true;
        int newmeta;

        if (decay > 0) {
            numAdjacentSources = 0;
            int smallestDecay = getSmallestFlowDecay(world, x - 1, y, z, -100);
            smallestDecay = getSmallestFlowDecay(world, x + 1, y, z, smallestDecay);
            smallestDecay = getSmallestFlowDecay(world, x, y, z - 1, smallestDecay);
            smallestDecay = getSmallestFlowDecay(world, x, y, z + 1, smallestDecay);
            newmeta = smallestDecay + b0;

            if (newmeta >= 8 || smallestDecay < 0) {
                newmeta = -1;
            }

            if (getFlowDecay(world, x, y + 1, z) >= 0) {
                int decayAbove = getFlowDecay(world, x, y + 1, z);

                if (decayAbove >= 8) {
                    newmeta = decayAbove;
                } else {
                    newmeta = decayAbove + 8;
                }
            }

            if (!ModConfig.waterMechanics
                    && numAdjacentSources >= 2
                    && blockMaterial == Material.water
                    && (world.getBlockMaterial(x, y - 1, z).isSolid() || world.getBlockMaterial(x, y - 1, z) == blockMaterial
                            && world.getBlockMetadata(x, y - 1, z) == 0)) newmeta = 0;

            if (blockMaterial == Material.lava && decay < 8 && newmeta < 8 && newmeta > decay && rnd.nextInt(4) != 0) {
                newmeta = decay;
                flag = false;
            }

            if (newmeta == decay) {
                if (flag) {
                    liquid.setStationary(world, x, y, z);
                }
            } else {
                decay = newmeta;

                if (newmeta < 0) {
                    world.setBlockToAir(x, y, z);
                } else {
                    world.setBlockMetadataWithNotify(x, y, z, newmeta, 2);
                    world.scheduleBlockUpdate(x, y, z, blockID, tickRate(world));
                    world.notifyBlocksOfNeighborChange(x, y, z, blockID);
                }
            }
        } else {
            liquid.setStationary(world, x, y, z);
        }

        if (ModConfig.waterMechanics && liquid.canTransferLiquid(world, x, y - 1, z) && liquid.canFlowOnPiston(world, x, y, z)
                && liquid.removeSource(world, x, y, z, false, false)) {
            liquid.transferLiquid(world, x, y - 1, z);
            liquid.notifyLiquidAbove(world, x, y, z);
        } else if (liquid.canDisplaceBlock(world, x, y - 1, z, liquid.isSource(blockID, world.getBlockMetadata(x, y, z)))) {
            if (blockMaterial == Material.lava && world.getBlockMaterial(x, y - 1, z) == Material.water) {
                world.setBlock(x, y - 1, z, Block.stone.blockID);
                triggerLavaMixEffects(world, x, y - 1, z);
                return;
            }

            if (decay >= 8) {
                flowIntoBlock(world, x, y - 1, z, decay);
            } else {
                flowIntoBlock(world, x, y - 1, z, decay + 8);
            }
        } else if (decay >= 0 && (decay == 0 || liquid.blockBlocksFlow(world, x, y - 1, z))) {
            boolean[] aboolean = getOptimalFlowDirections(world, x, y, z);
            newmeta = decay + b0;

            if (decay >= 8) {
                newmeta = 1;
            }

            if (newmeta >= 8) {
                return;
            }

            if (aboolean[0] && liquid.canWaterSpreadSideways(world, x, y, z, -1, 0)) {
                flowIntoBlock(world, x - 1, y, z, newmeta);
            }

            if (aboolean[1] && liquid.canWaterSpreadSideways(world, x, y, z, 1, 0)) {
                flowIntoBlock(world, x + 1, y, z, newmeta);
            }

            if (aboolean[2] && liquid.canWaterSpreadSideways(world, x, y, z, 0, -1)) {
                flowIntoBlock(world, x, y, z - 1, newmeta);
            }

            if (aboolean[3] && liquid.canWaterSpreadSideways(world, x, y, z, 0, 1)) {
                flowIntoBlock(world, x, y, z + 1, newmeta);
            }
        }
    }

    /**
     * flowIntoBlock(World world, int x, int y, int z, int newFlowDecay) - Flows
     * into the block at the coordinates and changes the block type to the
     * liquid.
     */
    private void flowIntoBlock(World world, int x, int y, int z, int newFlowDecay) {
        if (liquid.canDisplaceBlock(world, x, y, z)) {
            int id = world.getBlockId(x, y, z);

            if (id > 0) {
                if (blockMaterial == Material.lava) {
                    triggerLavaMixEffects(world, x, y, z);
                } else {
                    Block.blocksList[id].dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                }
            }

            world.setBlock(x, y, z, blockID, newFlowDecay, ModInfo.NOTIFY_MARKBLOCK + ModInfo.NOTIFY_NEIGHBORS);
        }
    }

    /**
     * calculateFlowCost(World world, int x, int y, int z, int accumulatedCost,
     * int previousDirectionOfFlow) - Used to determine the path of least
     * resistance, this method returns the lowest possible flow cost for the
     * direction of flow indicated. Each necessary horizontal flow adds to the
     * flow cost.
     */
    private int calculateFlowCost(World world, int x, int y, int z, int accCost, int prevDir) {
        int cost = 1000;

        for (int dir = 0; dir < 4; ++dir) {
            if ((dir != 0 || prevDir != 1) && (dir != 1 || prevDir != 0) && (dir != 2 || prevDir != 3) && (dir != 3 || prevDir != 2)) {
                int l1 = x;
                int i2 = z;

                if (dir == 0) {
                    l1 = x - 1;
                }

                if (dir == 1) {
                    ++l1;
                }

                if (dir == 2) {
                    i2 = z - 1;
                }

                if (dir == 3) {
                    ++i2;
                }

                if (!liquid.blockBlocksFlow(world, l1, y, i2)
                        && (world.getBlockMaterial(l1, y, i2) != blockMaterial || world.getBlockMetadata(l1, y, i2) != 0)) {
                    if (!liquid.blockBlocksFlow(world, l1, y - 1, i2)) {
                        return accCost;
                    }

                    if (accCost < 4) {
                        int j2 = calculateFlowCost(world, l1, y, i2, accCost + 1, dir);

                        if (j2 < cost) {
                            cost = j2;
                        }
                    }
                }
            }
        }

        return cost;
    }

    /**
     * Returns a boolean array indicating which flow directions are optimal
     * based on each direction's calculated flow cost. Each array index
     * corresponds to one of the four cardinal directions. A value of true
     * indicates the direction is optimal.
     */
    private boolean[] getOptimalFlowDirections(World world, int x, int y, int z) {
        int prevDir;
        int xOff;

        for (prevDir = 0; prevDir < 4; ++prevDir) {
            flowCost[prevDir] = 1000;
            xOff = x;
            int zOff = z;

            if (prevDir == 0) {
                xOff = x - 1;
            }

            if (prevDir == 1) {
                ++xOff;
            }

            if (prevDir == 2) {
                zOff = z - 1;
            }

            if (prevDir == 3) {
                ++zOff;
            }

            if (!liquid.blockBlocksFlow(world, xOff, y, zOff)
                    && (world.getBlockMaterial(xOff, y, zOff) != blockMaterial || world.getBlockMetadata(xOff, y, zOff) != 0)) {
                if (liquid.blockBlocksFlow(world, xOff, y - 1, zOff)) {
                    flowCost[prevDir] = calculateFlowCost(world, xOff, y, zOff, 1, prevDir);
                } else {
                    flowCost[prevDir] = 0;
                }
            }
        }

        prevDir = flowCost[0];

        for (xOff = 1; xOff < 4; ++xOff) {
            if (flowCost[xOff] < prevDir) {
                prevDir = flowCost[xOff];
            }
        }

        for (xOff = 0; xOff < 4; ++xOff) {
            isOptimalFlowDirection[xOff] = flowCost[xOff] == prevDir;
        }

        return isOptimalFlowDirection;
    }

    /**
     * getSmallestFlowDecay(World world, intx, int y, int z, int
     * currentSmallestFlowDecay) - Looks up the flow decay at the coordinates
     * given and returns the smaller of this value or the provided
     * currentSmallestFlowDecay. If one value is valid and the other isn't, the
     * valid value will be returned. Valid values are >= 0. Flow decay is the
     * amount that a liquid has dissipated. 0 indicates a source block.
     */
    @Override
    protected int getSmallestFlowDecay(World world, int x, int y, int z, int currSmallestDecay) {
        int decay = getFlowDecay(world, x, y, z);

        if (decay < 0) {
            return currSmallestDecay;
        } else {
            if (decay == 0) {
                ++numAdjacentSources;
            }

            if (decay >= 8) {
                decay = 0;
            }

            return currSmallestDecay >= 0 && decay >= currSmallestDecay ? currSmallestDecay : decay;
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (world.getBlockId(x, y, z) == blockID) {
            world.scheduleBlockUpdate(x, y, z, blockID, tickRate(world));
        }
    }

    @Override
    public boolean func_82506_l() {
        return false;
    }
}
