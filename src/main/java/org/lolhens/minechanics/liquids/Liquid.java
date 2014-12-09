package org.lolhens.minechanics.liquids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lolhens.minechanics.core.config.MainConfig;
import org.lolhens.minechanics.core.reference.Notify;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;

public class Liquid {
    private static Map<Block, IFlowingLiquidHandler> liquidHandlers = new HashMap<Block, IFlowingLiquidHandler>();
    private static Map<Block, Integer> waterResistant = new HashMap<Block, Integer>();
    private static Map<Block, Integer> notWaterproof = new HashMap<Block, Integer>();
    private static Map<Block, Integer> smoothLiquidTransitions = new HashMap<Block, Integer>();
    private static List<Liquid> liquids = new ArrayList<Liquid>();

    public String name;
    public Block moving, stable;

    public static Liquid liquid_water;
    public static Liquid liquid_lava;

    @EventListener({ "init" })
    public static void init(Event event) {
        liquid_water = new Liquid(Blocks.flowing_water, Blocks.water);
        liquid_lava = new Liquid(Blocks.flowing_lava, Blocks.lava);
        waterResistant.put(Blocks.wooden_door, -1);
        waterResistant.put(Blocks.iron_door, -1);
        waterResistant.put(Blocks.wall_sign, -1);
        waterResistant.put(Blocks.ladder, -1);
        waterResistant.put(Blocks.reeds, -1);
        notWaterproof.put(Blocks.leaves, -1);
        notWaterproof.put(Blocks.leaves2, -1);
        smoothLiquidTransitions.put(Blocks.glass, -1);
    }

    public static final int MOVING = 1;
    public static final int STABLE = 2;
    public static final int SOURCE = 4;
    public static final int FLOWING = 8;
    public static final int FALLING = 16;

    public Liquid(Block moving, Block stationary) {
        this.moving = moving;
        stable = stationary;
        liquids.add(this);
    }

    public static Liquid getRegisteredLiquid(Block block) {
        for (Liquid liquid : liquids) {
            if (liquid.moving == block || liquid.stable == block) return liquid;
        }
        return null;
    }

    public static boolean isRegisteredLiquid(Block block) {
        return getRegisteredLiquid(block) != null;
    }

    public boolean isLiquid(Block block, int meta, int liquidFlags) {
        if ((liquidFlags & MOVING + STABLE) == 0) liquidFlags += MOVING + STABLE;
        if ((liquidFlags & SOURCE + FLOWING + FALLING) == 0) liquidFlags += SOURCE + FLOWING + FALLING;
        return ((liquidFlags & MOVING) == MOVING && block == moving || (liquidFlags & STABLE) == STABLE && block == stable)
                && ((liquidFlags & SOURCE) == SOURCE && meta == 0 || (liquidFlags & FLOWING) == FLOWING && meta > 0 && meta < 8 || (liquidFlags & FALLING) == FALLING
                        && meta >= 8);
    }

    public boolean isLiquid(IBlockAccess blockAccess, int x, int y, int z, int liquidFlags) {
        return isLiquid(blockAccess.getBlock(x, y, z), blockAccess.getBlockMetadata(x, y, z), liquidFlags);
    }

    public void setLiquid(World world, int x, int y, int z, int liquidFlags, int meta, int updateFlags) {
        // Block myBlock = world.getBlock(x, y, z);
        if ((liquidFlags & MOVING + STABLE) != 0
                || !isLiquid(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), MOVING + STABLE)) {
            world.setBlock(
                    x,
                    y,
                    z,
                    (liquidFlags & MOVING) == MOVING ? moving : stable,
                    meta >= 0 ? meta : isLiquid(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), 0) ? world
                            .getBlockMetadata(x, y, z) : 0, updateFlags);
            // world.scheduleBlockUpdate(x, y, z, myBlock,
            // myBlock.tickRate(world));
            // world.notifyBlocksOfNeighborChange(x, y, z, myBlock);
        } else {
            world.setBlockMetadataWithNotify(x, y, z, meta, updateFlags);
            // world.scheduleBlockUpdate(x, y, z, myBlock,
            // myBlock.tickRate(world));
            // world.notifyBlocksOfNeighborChange(x, y, z, myBlock);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Liquid) {
            return obj == this;
        } else if (obj instanceof Block) {
            return moving == (Block) obj || stable == (Block) obj;
        }
        return false;
    }

    public Material getMaterial() {
        return moving.getMaterial();
    }

    public void update(World world, int x, int y, int z) {
        if (isLiquid(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), 0)) {
            setLiquid(world, x, y, z, MOVING, -1, Notify.MARKBLOCK + Notify.NEIGHBORS);
            world.notifyBlockChange(x, y, z, moving);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }
    }

    public boolean canDisplaceLiquid(World world, int x, int y, int z) {
        if (canTransferLiquid(world, x, y - 1, z) || canTransferLiquid(world, x + 1, y, z)
                || canTransferLiquid(world, x - 1, y, z) || canTransferLiquid(world, x, y, z + 1)
                || canTransferLiquid(world, x, y, z - 1) || canTransferLiquid(world, x, y + 1, z)) {
            return true;
        }
        return false;
    }

    public boolean displaceLiquid(World world, int x, int y, int z) {
        if (transferLiquid(world, x, y - 1, z) || transferLiquid(world, x + 1, y, z) || transferLiquid(world, x - 1, y, z)
                || transferLiquid(world, x, y, z + 1) || transferLiquid(world, x, y, z - 1) || transferLiquid(world, x, y + 1, z)) {
            setLiquid(world, x, y, z, 0, 7, Notify.MARKBLOCK + Notify.NEIGHBORS);
            return true;
        }
        return false;
    }

    public static void registerFlowingLiquidHandler(Block block, IFlowingLiquidHandler liquidHandler) {
        liquidHandlers.put(block, liquidHandler);
    }

    public static void unregisterFlowingLiquidHandler(Block block) {
        if (liquidHandlers.containsKey(block)) liquidHandlers.remove(block);
    }

    public boolean transferLiquid(World world, int x, int y, int z) {
        Block myBlock = world.getBlock(x, y, z);
        int myMeta = world.getBlockMetadata(x, y, z);
        int modeCheck = canTransferLiquidRaw(world, x, y, z);
        if (modeCheck == 1) {
            return liquidHandlers.get(myBlock).receiveLiquid(world, x, y, z, this, myMeta, 0);
        } else if (modeCheck == 2) {
            if (!myBlock.isAir(world, x, y, z) && !(myBlock instanceof BlockLiquid)) {
                myBlock.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            }
            setLiquid(world, x, y, z, MOVING, 0, Notify.NEIGHBORS);
            return true;
        }
        return false;
    }

    public boolean canTransferLiquid(World world, int x, int y, int z) {
        return canTransferLiquidRaw(world, x, y, z) > 0;
    }

    public int canTransferLiquidRaw(World world, int x, int y, int z) {
        Block myBlock = world.getBlock(x, y, z);
        int myMeta = world.getBlockMetadata(x, y, z);
        if (liquidHandlers.containsKey(myBlock) && liquidHandlers.get(myBlock).canReceiveLiquid(world, x, y, z, this, myMeta, 0)) {
            return 1;
        } else if (canDisplaceBlock(myBlock, myMeta) || isLiquid(myBlock, myMeta, FLOWING + FALLING)) {
            return 2;
        }
        return 0;
    }

    public void notifyLiquidAbove(World world, int x, int y, int z) {
        if (canTransferLiquidRaw(world, x, y + 1, z) != 1) return;
        int yOffset = 2;
        while (canTransferLiquidRaw(world, x, y + yOffset, z) == 1)
            yOffset++;
        if (isLiquid(world, x, y + yOffset, z, STABLE))

        update(world, x, y + yOffset, z);
    }

    public boolean removeSource(World world, int x, int y, int z, boolean ignoreHeight, boolean readOnly, int maxBlocks) {
        return MainConfig.liquid_mechanicEnabled
                && removeSource(world, x, y, z, 0, 0, ignoreHeight, readOnly, maxBlocks, new LinkedList<int[]>()) == 2;
    }

    private int removeSource(World world, int x, int y, int z, int dir, int ret, boolean ignoreHeight, boolean readOnly,
            int maxBlocks, List<int[]> useless) {
        if (maxBlocks <= 0) return 0;
        if (isLiquid(world, x, y, z, 0)) {
            for (int[] uselessPos : useless)
                if (uselessPos[0] == x && uselessPos[2] == z && uselessPos[1] == y) return 0;
            useless.add(new int[] { x, y, z });

            int[] metalist = { -1, -1, -1, -1, -1, -1 };

            metalist[0] = world.getBlockMetadata(x, y, z);
            if (isLiquid(world, x, y + 1, z, 0)) metalist[1] = world.getBlockMetadata(x, y + 1, z);
            if (isLiquid(world, x + 1, y, z, 0)) metalist[2] = world.getBlockMetadata(x + 1, y, z);
            if (isLiquid(world, x - 1, y, z, 0)) metalist[3] = world.getBlockMetadata(x - 1, y, z);
            if (isLiquid(world, x, y, z + 1, 0)) metalist[4] = world.getBlockMetadata(x, y, z + 1);
            if (isLiquid(world, x, y, z - 1, 0)) metalist[5] = world.getBlockMetadata(x, y, z - 1);

            ret = foundSource(world, x, y, z, metalist[0], readOnly);
            if (ret > 0) return ret;
            ret = foundSource(world, x, y + 1, z, metalist[1], readOnly);
            if (ret > 0) return ret;
            ret = foundSource(world, x + 1, y, z, metalist[2], readOnly);
            if (ret > 0) return ret;
            ret = foundSource(world, x - 1, y, z, metalist[3], readOnly);
            if (ret > 0) return ret;
            ret = foundSource(world, x, y, z + 1, metalist[4], readOnly);
            if (ret > 0) return ret;
            ret = foundSource(world, x, y, z - 1, metalist[5], readOnly);
            if (ret > 0) return ret;

            if (metalist[1] >= 8 || metalist[0] >= 8) {
                ret = removeSource(world, x, y + 1, z, 0, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                if (ret > 0) return ret;
            }
            if (dir != 2 && metalist[0] < 8 && (metalist[2] >= 8 || metalist[2] < metalist[0])) {
                ret = removeSource(world, x + 1, y, z, 1, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                if (ret > 0) return ret;
            }
            if (dir != 1 && metalist[0] < 8 && (metalist[3] >= 8 || metalist[3] < metalist[0])) {
                ret = removeSource(world, x - 1, y, z, 2, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                if (ret > 0) return ret;
            }
            if (dir != 4 && metalist[0] < 8 && (metalist[4] >= 8 || metalist[4] < metalist[0])) {
                ret = removeSource(world, x, y, z + 1, 3, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                if (ret > 0) return ret;
            }
            if (dir != 3 && metalist[0] < 8 && (metalist[5] >= 8 || metalist[5] < metalist[0])) {
                ret = removeSource(world, x, y, z - 1, 4, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                if (ret > 0) return ret;
            }

            if (ignoreHeight) {
                if (dir != 2 && metalist[0] < 8 && metalist[2] < 8 && ignoreHeight) {
                    ret = removeSource(world, x + 1, y, z, 1, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                    if (ret > 0) return ret;
                }
                if (dir != 1 && metalist[0] < 8 && metalist[3] < 8 && ignoreHeight) {
                    ret = removeSource(world, x - 1, y, z, 2, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                    if (ret > 0) return ret;
                }
                if (dir != 4 && metalist[0] < 8 && metalist[4] < 8 && ignoreHeight) {
                    ret = removeSource(world, x, y, z + 1, 3, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                    if (ret > 0) return ret;
                }
                if (dir != 3 && metalist[0] < 8 && metalist[5] < 8 && ignoreHeight) {
                    ret = removeSource(world, x, y, z - 1, 4, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                    if (ret > 0) return ret;
                }
            }
        }
        return 0;
    }

    private int foundSource(World world, int x, int y, int z, int meta, boolean readOnly) {
        if (meta == -1 || meta > 0) return 0;
        Block myBlock = world.getBlock(x, y - 1, z);
        if (MainConfig.liquid_vanillaMechanicSpawner != null && myBlock == MainConfig.liquid_vanillaMechanicSpawner) return 1;
        if ((MainConfig.liquid_infiniteLiquidSource == null || myBlock != MainConfig.liquid_infiniteLiquidSource) && !readOnly) {
            setLiquid(world, x, y, z, 0, 1, Notify.NEIGHBORS);
        }
        return 2;
    }

    public boolean canDisplaceBlock(IBlockAccess iba, int x, int y, int z) {
        return canDisplaceBlock(iba.getBlock(x, y, z), iba.getBlockMetadata(x, y, z));
    }

    public boolean canDisplaceBlock(Block block, int meta) {
        return !isRegisteredLiquid(block) && !blockBlocksFlow(block, meta);
    }

    public boolean canDisplaceBlock(IBlockAccess iba, int x, int y, int z, boolean isSource) {
        return canDisplaceBlock(iba.getBlock(x, y, z), iba.getBlockMetadata(x, y, z), isSource);
    }

    public boolean canDisplaceBlock(Block block, int meta, boolean isSource) {
        return (!isRegisteredLiquid(block) || isSource && isLiquid(block, meta, FLOWING)) && !blockBlocksFlow(block, meta);
    }

    public boolean blockBlocksFlow(Block block, int meta) {
        return block != Blocks.air
                && (MainConfig.liquid_mechanicEnabled && meta > -1 && isLiquid(block, meta, SOURCE)
                        || isBlockContained(waterResistant, block, meta) || block.getMaterial().blocksMovement());
    }

    public boolean blockBlocksFlow(IBlockAccess iba, int x, int y, int z) {
        return blockBlocksFlow(iba.getBlock(x, y, z), iba.getBlockMetadata(x, y, z));
    }

    public boolean canFlowOnPiston(World world, int x, int y, int z) {
        Block hiddenBlock = world.getBlock(x, y - 2, z);
        if (MainConfig.liquid_mechanicEnabled && (hiddenBlock == Blocks.piston || hiddenBlock == Blocks.piston_extension)
                && new Random().nextInt(5) != 0) {
            update(world, x, y, z);
            world.scheduleBlockUpdate(x, y, z, moving, 20);
            return false;
        }
        return true;
    }

    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return !(isLiquid(blockAccess, x, y, z, 0) || side == 1 || isBlockContained(smoothLiquidTransitions,
                blockAccess.getBlock(x, y, z), blockAccess.getBlockMetadata(x, y, z)));
    }

    public static boolean isBlockContained(Map<Block, Integer> map, Block block, int meta) {
        return map.containsKey(block) && (map.get(block) == -1 || map.get(block) == meta);
    }
}