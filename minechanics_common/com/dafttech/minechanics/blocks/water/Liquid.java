package com.dafttech.minechanics.blocks.water;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.client.renderer.BlockRenderer;
import com.dafttech.minechanics.data.ModConfig;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.interfaces.IFlowingLiquidHandler;
import com.dafttech.minechanics.interfaces.ISmoothWaterTransition;

public class Liquid {
    public static final Liquid instance = new Liquid();

    public String name;
    public int moving, stationary;
    private static List<Liquid> liquids = new ArrayList<Liquid>();
    public static boolean[] waterResistant = new boolean[Block.blocksList.length];
    private static Map<Integer, IFlowingLiquidHandler> liquidHandlerMap = new HashMap<Integer, IFlowingLiquidHandler>();

    public static final Liquid LIQUID_WATER = new Liquid("water", Block.waterMoving.blockID);
    public static final Liquid LIQUID_LAVA = new Liquid("lava", Block.lavaMoving.blockID);

    @EventListener("INIT")
    public void init(Event event) {
        waterResistant[Block.doorWood.blockID] = true;
        waterResistant[Block.doorIron.blockID] = true;
        waterResistant[Block.signPost.blockID] = true;
        waterResistant[Block.ladder.blockID] = true;
        waterResistant[Block.reed.blockID] = true;
    }

    private Liquid() {
    }

    public Liquid(String name, int moving) {
        this(name, moving, moving + 1);
    }

    public Liquid(String name, int moving, int stationary) {
        this.name = name;
        this.moving = moving;
        this.stationary = stationary;
        liquids.add(this);
    }

    public static Liquid getById(int id) {
        for (Liquid liquid : liquids) {
            if (liquid.moving == id || liquid.stationary == id) return liquid;
        }
        return null;
    }

    public static boolean isLiquidAtAll(IBlockAccess world, int x, int y, int z) {
        return isLiquidAtAll(world.getBlockId(x, y, z));
    }

    public static boolean isLiquidAtAll(int id) {
        for (Liquid liquid : liquids) {
            if (liquid.isLiquid(id)) return true;
        }
        return false;
    }

    public boolean isLiquid(IBlockAccess iba, int x, int y, int z) {
        return isLiquid(iba.getBlockId(x, y, z));
    }

    public boolean isLiquid(int id) {
        return id == moving || id == stationary;
    }

    public boolean isStationary(IBlockAccess iba, int x, int y, int z) {
        return isStationary(iba.getBlockId(x, y, z));
    }

    public boolean isStationary(int id) {
        return id == stationary;
    }

    public boolean isMoving(IBlockAccess iba, int x, int y, int z) {
        return isMoving(iba.getBlockId(x, y, z));
    }

    public boolean isMoving(int id) {
        return id == moving;
    }

    public static boolean isSourceAtAll(IBlockAccess iba, int x, int y, int z) {
        return isSourceAtAll(iba.getBlockId(x, y, z), iba.getBlockMetadata(x, y, z));
    }

    public static boolean isSourceAtAll(int id, int meta) {
        return isLiquidAtAll(id) && meta == 0;
    }

    public boolean isSource(IBlockAccess iba, int x, int y, int z) {
        return isSource(iba.getBlockId(x, y, z), iba.getBlockMetadata(x, y, z));
    }

    public boolean isSource(int id, int meta) {
        return isLiquid(id) && meta == 0;
    }

    public boolean isFlowing(IBlockAccess iba, int x, int y, int z) {
        return isFlowing(iba.getBlockId(x, y, z), iba.getBlockMetadata(x, y, z));
    }

    public boolean isFlowing(int id, int meta) {
        return isLiquid(id) && meta > 0;
    }

    public boolean isOnlyFlowing(int id, int meta) {
        return isLiquid(id) && meta > 0 && meta < 8;
    }

    public boolean isFalling(IBlockAccess iba, int x, int y, int z) {
        return isFalling(iba.getBlockId(x, y, z), iba.getBlockMetadata(x, y, z));
    }

    public boolean isFalling(int id, int meta) {
        return isLiquid(id) && meta >= 8;
    }

    public boolean isPillarFalling(IBlockAccess iba, int x, int y, int z) {
        int myId = iba.getBlockId(x, y, z), myMeta = iba.getBlockMetadata(x, y, z);
        return isFalling(myId, myMeta) || isOnlyFlowing(myId, myMeta) && isFalling(iba, x, y - 1, z);
    }

    public boolean isOnGround(int id, int meta) {
        return isLiquid(id) && meta < 8;
    }

    public boolean isOnGround(IBlockAccess iba, int x, int y, int z) {
        return isOnGround(iba.getBlockId(x, y, z), iba.getBlockMetadata(x, y, z));
    }

    public boolean isPillarOnGround(IBlockAccess iba, int x, int y, int z) {
        return isLiquid(iba, x, y, z) && !isPillarFalling(iba, x, y, z);
    }

    public boolean isFlowingOnGround(int id, int meta) {
        if (isLiquid(id) && meta > 0 && meta < 8) return true;
        return false;
    }

    public boolean isFlowingOnGround(IBlockAccess iba, int x, int y, int z) {
        return isFlowingOnGround(iba.getBlockId(x, y, z), iba.getBlockMetadata(x, y, z));
    }

    public Material getMaterial() {
        return Block.blocksList[moving].blockMaterial;
    }

    public boolean isLiquid(String name) {
        return name.toLowerCase().equals(name.toLowerCase());
    }

    public String getName() {
        return name;
    }

    public void update(World world, int x, int y, int z) {
        if (isLiquid(world, x, y, z)) {
            setMoving(world, x, y, z);
            world.notifyBlockChange(x, y, z, moving);
            world.markBlockForRenderUpdate(x, y, z);
        }
    }

    public void setFlowing(World world, int x, int y, int z) {
        if (isSource(world, x, y, z)) {
            if (ModConfig.fancyWaterUpdates) {
                world.setBlockMetadataWithNotify(x, y, z, 1, ModInfo.NOTIFY_MARKBLOCK + ModInfo.NOTIFY_NEIGHBORS);
            } else {
                world.setBlockMetadataWithNotify(x, y, z, 1, ModInfo.NOTIFY_MARKBLOCK);
            }
            update(world, x, y, z);
        }
    }

    public void setSource(World world, int x, int y, int z) {
        if (isFlowing(world, x, y, z)) {
            if (ModConfig.fancyWaterUpdates) {
                world.setBlockMetadataWithNotify(x, y, z, 0, ModInfo.NOTIFY_MARKBLOCK + ModInfo.NOTIFY_NEIGHBORS);
            } else {
                world.setBlockMetadataWithNotify(x, y, z, 0, ModInfo.NOTIFY_MARKBLOCK);
            }
            update(world, x, y, z);
        }
    }

    public void setMoving(World world, int x, int y, int z) {
        world.setBlock(x, y, z, moving, world.getBlockMetadata(x, y, z), ModInfo.NOTIFY_MARKBLOCK);
        world.scheduleBlockUpdate(x, y, z, moving, Block.blocksList[stationary].tickRate(world));
        // world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
    }

    public void setStationary(World world, int x, int y, int z) {
        world.setBlock(x, y, z, stationary, world.getBlockMetadata(x, y, z), ModInfo.NOTIFY_MARKBLOCK);
        // world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
    }

    public boolean canDisplaceLiquid(World world, int x, int y, int z) {
        if (canTransferLiquid(world, x, y - 1, z) || canTransferLiquid(world, x + 1, y, z) || canTransferLiquid(world, x - 1, y, z)
                || canTransferLiquid(world, x, y, z + 1) || canTransferLiquid(world, x, y, z - 1) || canTransferLiquid(world, x, y + 1, z)) {
            return true;
        }
        return false;
    }

    public boolean displaceLiquid(World world, int x, int y, int z) {
        if (transferLiquid(world, x, y - 1, z) || transferLiquid(world, x + 1, y, z) || transferLiquid(world, x - 1, y, z)
                || transferLiquid(world, x, y, z + 1) || transferLiquid(world, x, y, z - 1) || transferLiquid(world, x, y + 1, z)) {
            setFlowing(world, x, y, z);
            return true;
        }
        return false;
    }

    public static void registerFlowingLiquidHandler(IFlowingLiquidHandler liquidHandler, int blockId) {
        liquidHandlerMap.put(blockId, liquidHandler);
    }

    public static void registerFlowingLiquidHandler(Block block) {
        if (block instanceof IFlowingLiquidHandler) registerFlowingLiquidHandler((IFlowingLiquidHandler) block, block.blockID);
    }

    public static void unregisterFlowingLiquidHandler(int blockId) {
        if (liquidHandlerMap.containsKey(blockId)) liquidHandlerMap.remove(blockId);
    }

    public boolean transferLiquid(World world, int x, int y, int z) {
        int myId = world.getBlockId(x, y, z);
        int myMeta = world.getBlockMetadata(x, y, z);
        int modeCheck = canTransferLiquidRaw(world, x, y, z);
        if (modeCheck == 1) {
            return liquidHandlerMap.get(myId).receiveLiquid(world, x, y, z, myId, myMeta, this);
        } else if (modeCheck == 2) {
            if (myId > 0 && !(Block.blocksList[myId] instanceof BlockFluid)) {
                Block.blocksList[myId].dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            }
            if (ModConfig.fancyWaterUpdates) {
                world.setBlock(x, y, z, moving, 0, ModInfo.NOTIFY_MARKBLOCK + ModInfo.NOTIFY_NEIGHBORS);
            } else {
                world.setBlock(x, y, z, moving, 0, ModInfo.NOTIFY_MARKBLOCK);
            }
            return true;
        }
        return false;
    }

    public boolean canTransferLiquid(World world, int x, int y, int z) {
        return canTransferLiquidRaw(world, x, y, z) > 0;
    }

    public int canTransferLiquidRaw(World world, int x, int y, int z) {
        int myId = world.getBlockId(x, y, z);
        int myMeta = world.getBlockMetadata(x, y, z);
        if (liquidHandlerMap.containsKey(myId) && liquidHandlerMap.get(myId).canReceiveLiquid(world, x, y, z, myId, myMeta, this)) {
            return 1;
        } else if (canDisplaceBlock(myId, myMeta) || isFlowing(myId, myMeta)) {
            return 2;
        }
        return 0;
    }

    public void notifyLiquidAbove(World world, int x, int y, int z) {
        if (canTransferLiquidRaw(world, x, y + 1, z) != 1) return;
        int yOffset = 2;
        while (canTransferLiquidRaw(world, x, y + yOffset, z) == 1)
            yOffset++;
        /* if (isStationary(world, x, y + yOffset, z)) */update(world, x, y + yOffset, z);
    }

    @SuppressWarnings("unused")
    private static int rec = 0;

    public boolean removeSource(World world, int x, int y, int z, boolean ignoreHeight, boolean readOnly) {
        rec = 0;
        return ModConfig.waterMechanics && WaterMechanics.isEnabledWaterCalculation
                && removeSource(world, x, y, z, 0, 0, ignoreHeight, readOnly, new ArrayList<int[]>()) == 2;
    }

    private int removeSource(World world, int x, int y, int z, int dir, int ret, boolean ignoreHeight, boolean readOnly, List<int[]> useless) {
        rec++;
        if (isLiquid(world, x, y, z)) {
            for (int[] uselessPos : useless)
                if (uselessPos[0] == x && uselessPos[2] == z && uselessPos[1] == y) return 0;
            useless.add(new int[] { x, y, z });

            int[] metalist = { -1, -1, -1, -1, -1, -1 };

            metalist[0] = world.getBlockMetadata(x, y, z);
            if (isLiquid(world, x, y + 1, z)) metalist[1] = world.getBlockMetadata(x, y + 1, z);
            if (isLiquid(world, x + 1, y, z)) metalist[2] = world.getBlockMetadata(x + 1, y, z);
            if (isLiquid(world, x - 1, y, z)) metalist[3] = world.getBlockMetadata(x - 1, y, z);
            if (isLiquid(world, x, y, z + 1)) metalist[4] = world.getBlockMetadata(x, y, z + 1);
            if (isLiquid(world, x, y, z - 1)) metalist[5] = world.getBlockMetadata(x, y, z - 1);

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
                ret = removeSource(world, x, y + 1, z, 0, 0, ignoreHeight, readOnly, useless);
                if (ret > 0) return ret;
            }
            if (dir != 2 && metalist[0] < 8 && (metalist[2] >= 8 || metalist[2] < metalist[0])) {
                ret = removeSource(world, x + 1, y, z, 1, 0, ignoreHeight, readOnly, useless);
                if (ret > 0) return ret;
            }
            if (dir != 1 && metalist[0] < 8 && (metalist[3] >= 8 || metalist[3] < metalist[0])) {
                ret = removeSource(world, x - 1, y, z, 2, 0, ignoreHeight, readOnly, useless);
                if (ret > 0) return ret;
            }
            if (dir != 4 && metalist[0] < 8 && (metalist[4] >= 8 || metalist[4] < metalist[0])) {
                ret = removeSource(world, x, y, z + 1, 3, 0, ignoreHeight, readOnly, useless);
                if (ret > 0) return ret;
            }
            if (dir != 3 && metalist[0] < 8 && (metalist[5] >= 8 || metalist[5] < metalist[0])) {
                ret = removeSource(world, x, y, z - 1, 4, 0, ignoreHeight, readOnly, useless);
                if (ret > 0) return ret;
            }

            if (ignoreHeight) {
                if (dir != 2 && metalist[0] < 8 && metalist[2] < 8 && ignoreHeight) {
                    ret = removeSource(world, x + 1, y, z, 1, 0, ignoreHeight, readOnly, useless);
                    if (ret > 0) return ret;
                }
                if (dir != 1 && metalist[0] < 8 && metalist[3] < 8 && ignoreHeight) {
                    ret = removeSource(world, x - 1, y, z, 2, 0, ignoreHeight, readOnly, useless);
                    if (ret > 0) return ret;
                }
                if (dir != 4 && metalist[0] < 8 && metalist[4] < 8 && ignoreHeight) {
                    ret = removeSource(world, x, y, z + 1, 3, 0, ignoreHeight, readOnly, useless);
                    if (ret > 0) return ret;
                }
                if (dir != 3 && metalist[0] < 8 && metalist[5] < 8 && ignoreHeight) {
                    ret = removeSource(world, x, y, z - 1, 4, 0, ignoreHeight, readOnly, useless);
                    if (ret > 0) return ret;
                }
            }
        }
        return 0;
    }

    private int foundSource(World world, int x, int y, int z, int meta, boolean readOnly) {
        if (meta == -1 || meta > 0) return 0;
        int myId = world.getBlockId(x, y - 1, z);
        if (myId == ModConfig.vanillaWaterSpawnerId) return 1;
        if (myId != ModConfig.infiniteSourceId && !readOnly) setFlowing(world, x, y, z);
        return 2;
    }

    public boolean canDisplaceBlock(IBlockAccess iba, int x, int y, int z) {
        return canDisplaceBlock(iba.getBlockId(x, y, z), iba.getBlockMetadata(x, y, z));
    }

    public boolean canDisplaceBlock(int id, int meta) {
        return !isLiquidAtAll(id) && !blockBlocksFlow(id, meta);
    }

    public boolean canDisplaceBlock(IBlockAccess iba, int x, int y, int z, boolean isSource) {
        return canDisplaceBlock(iba.getBlockId(x, y, z), iba.getBlockMetadata(x, y, z), isSource);
    }

    public boolean canDisplaceBlock(int id, int meta, boolean isSource) {
        return (!isLiquidAtAll(id) || isSource && isFlowing(id, meta)) && !blockBlocksFlow(id, meta);
    }

    public boolean blockBlocksFlow(int id, int meta) {
        return id != 0
                && (ModConfig.waterMechanics && meta > -1 && isSource(id, meta) || waterResistant[id] || Block.blocksList[id].blockMaterial
                        .blocksMovement());
    }

    public boolean blockBlocksFlow(IBlockAccess iba, int x, int y, int z) {
        return blockBlocksFlow(iba.getBlockId(x, y, z), iba.getBlockMetadata(x, y, z));
    }

    public boolean canWaterSpreadSideways(IBlockAccess iba, int x, int y, int z, int xOff, int zOff) {
        Liquid liquid = getById(iba.getBlockId(x, y, z));
        return !liquid.isLiquid(iba, x, y - 1, z) || liquid.isOnGround(iba, x, y, z);
        // if (ModConfig.waterMechanics &&
        // WaterMechanics.isEnabledWaterCalculation && isOnGround(iba, x, y, z)
        // && blockBlocksFlow(iba.getBlockId(x, y - 1, z), -1))
        // // TODO System.out.println("b+o: " + isOnGround(iba, x + xOff, y -
        // // 1, z
        // // + zOff) + "\nb+b+o: " + isOnGround(iba, x + xOff, y - 2, z +
        // // zOff));
        // if (ModConfig.waterMechanics &&
        // WaterMechanics.isEnabledWaterCalculation && isOnGround(iba, x, y, z)
        // && blockBlocksFlow(iba.getBlockId(x, y - 1, z), -1) ||
        // isPillarOnGround(iba, x + xOff, y - 1, z + zOff)
        // || blockBlocksFlow(iba, x + xOff, y - 1, z + zOff)) return true;
        // return false;
    }

    public boolean canFlowOnPiston(World world, int x, int y, int z) {
        int hiddenBlock = world.getBlockId(x, y - 2, z);
        if (ModConfig.waterMechanics && ModConfig.pistonMechanics
                && (hiddenBlock == Block.pistonBase.blockID || hiddenBlock == Block.pistonMoving.blockID) && new Random().nextInt(5) != 0) {
            update(world, x, y, z);
            world.scheduleBlockUpdate(x, y, z, moving, 20);
            return false;
        }
        return true;
    }

    public boolean shouldSideBeRendered(IBlockAccess iba, int x, int y, int z, int side) {
        Block block = Block.blocksList[iba.getBlockId(x, y, z)];
        boolean smoothTransition = block != null
                && (block instanceof ISmoothWaterTransition && ((ISmoothWaterTransition) block).isSmoothWaterTransition(iba, x, y, z) || BlockRenderer.smoothWaterTransitions
                        .contains(block) && isUnderWater(iba, x, y, z));
        Material var6 = iba.getBlockMaterial(x, y, z);
        return var6 == getMaterial() || smoothTransition ? false : side == 1 ? true : var6 == Material.ice ? false : Block.waterMoving
                .shouldSideBeRendered(iba, x, y, z, side);
    }

    @SuppressWarnings("unused")
    private boolean shouldSideBeRenderedAlternate(IBlockAccess world, int x, int y, int z, int side) {
        Material var6 = world.getBlockMaterial(x, y, z);
        return var6 == getMaterial() ? false : side == 1 ? true : var6 == Material.ice ? false : Block.stone.shouldSideBeRendered(world, x, y, z,
                side);
    }

    public boolean isUnderWater(IBlockAccess world, int x, int y, int z) {
        boolean ret = true;
        return ret;
    }
}
