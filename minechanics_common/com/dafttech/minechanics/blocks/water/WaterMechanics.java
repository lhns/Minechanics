package com.dafttech.minechanics.blocks.water;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import com.dafttech.minechanics.data.ModConfig;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.util.Effect;

public class WaterMechanics {
    public static int removeWaterOver = 0;
    public static int fillWaterUnder = 0;

    // ----------Environment----------// {
    public static void checkEnvironment(World world, int x, int y, int z) {
        if (Liquid.LIQUID_WATER.isLiquid(world, x, y, z) && ModConfig.environment) {
            if (Liquid.LIQUID_WATER.isLiquid(world, x, y + 1, z)) y++;
            if (rainCheck(world, x, y, z)) {
                if (Liquid.LIQUID_WATER.isFlowing(world, x + 1, y, z)) Liquid.LIQUID_WATER.setSource(world, x + 1, y, z);
                if (Liquid.LIQUID_WATER.isFlowing(world, x - 1, y, z)) Liquid.LIQUID_WATER.setSource(world, x - 1, y, z);
                if (Liquid.LIQUID_WATER.isFlowing(world, x, y, z + 1)) Liquid.LIQUID_WATER.setSource(world, x, y, z + 1);
                if (Liquid.LIQUID_WATER.isFlowing(world, x, y, z - 1)) Liquid.LIQUID_WATER.setSource(world, x, y, z - 1);
            }
            tryToDry(world, x, y, z);
        }
    }

    public static boolean rainCheck(World world, int x, int y, int z) {
        if (ModConfig.waterMechanics && world.canLightningStrikeAt(x, y + 1, z) && world.isRaining() && Liquid.LIQUID_WATER.isFlowing(world, x, y, z)
                && new Random().nextInt(ModConfig.rainRefillTries) == 0) {
            if (Liquid.LIQUID_WATER.isSource(world, x + 1, y, z) || Liquid.LIQUID_WATER.isSource(world, x - 1, y, z)
                    || Liquid.LIQUID_WATER.isSource(world, x, y, z + 1) || Liquid.LIQUID_WATER.isSource(world, x, y, z - 1)) {
                world.setBlockMetadataWithNotify(x, y, z, 0, ModInfo.NOTIFY_MARKBLOCK + ModInfo.NOTIFY_NEIGHBORS);
                return true;
            }
        }
        return false;
    }

    public static void tryToDry(World world, int x, int y, int z) {
        if (Liquid.LIQUID_WATER.isSource(world, x, y, z)
                && isCurrentlyDryInBiome(world, x, y, z)
                && (!Liquid.LIQUID_WATER.isSource(world, x + 1, y, z) || !Liquid.LIQUID_WATER.isSource(world, x - 1, y, z)
                        || !Liquid.LIQUID_WATER.isSource(world, x, y, z + 1) || !Liquid.LIQUID_WATER.isSource(world, x, y, z - 1))) {
            int sourcesBelow = 0;
            while (Liquid.LIQUID_WATER.isSource(world, x, y - (sourcesBelow + 1), z)) {
                sourcesBelow++;
            }
            if (new Random().nextInt((10 + sourcesBelow) * ModConfig.evaporationDelay + 1) == 0) {
                world.setBlockMetadataWithNotify(x, y, z, 1, ModInfo.NOTIFY_MARKBLOCK + ModInfo.NOTIFY_NEIGHBORS);
                Liquid.LIQUID_WATER.update(world, x, y, z);
                Effect.instance.evap(world, x, y, z);
            }
        }
    }

    // ----------Environment----------// }
    // ----------Biome----------// {
    public static boolean isCurrentlyDryInBiome(World world, int x, int y, int z) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        if (biome != null && !biome.getEnableSnow() && (!biome.canSpawnLightningBolt() || !world.isRaining())) {
            if (world.isDaytime() && world.getLightBrightness(x, y, z) > 0.5) {
                return true;
            }
        }
        return false;
    }

    // ----------Biome----------// }
    // ----------Other----------// {
    public static boolean ignoreWaterSourrceBlockingOnce = false;
    public static boolean isEnabledWaterCalculation = true;

    public static void blockDebug(World world, int x, int y, int z) {
        System.out.print(world.getBlockId(x, y, z) + " " + world.getBlockMetadata(x, y, z) + " - ");
        System.out.print(world.getBlockId(x + 1, y, z) + " " + world.getBlockMetadata(x + 1, y, z) + " - ");
        System.out.print(world.getBlockId(x - 1, y, z) + " " + world.getBlockMetadata(x - 1, y, z) + " - ");
        System.out.print(world.getBlockId(x, y, z + 1) + " " + world.getBlockMetadata(x, y, z + 1) + " - ");
        System.out.println(world.getBlockId(x, y, z - 1) + " " + world.getBlockMetadata(x, y, z - 1));
    }

    public static int flowUpdates, lastUpdatedUpdates, lastUpdatedTime, forcedDeact, biggestRec, highestRecTime, deactTime = 0, scheduledBlocks = 0;

    public static void calculateTicklag(World world, int x, int y, int z) {
        if (!ModConfig.tickLagPrevent || scheduledBlocks > 0) {
            isEnabledWaterCalculation = true;
            return;
        }
        int currentTime = (int) (System.currentTimeMillis() / 500);
        flowUpdates++;
        int tickDiff = flowUpdates - lastUpdatedUpdates;
        if (currentTime != lastUpdatedTime) {
            if (currentTime - deactTime > 600) forcedDeact = 0;
            if (tickDiff > ModConfig.maxTickBorder * (forcedDeact * 0.1 + 1) && isEnabledWaterCalculation) {
                isEnabledWaterCalculation = false;
                forcedDeact++;
                deactTime = currentTime;
            } else if (tickDiff < 60 * (forcedDeact * 0.01 + 1) && !isEnabledWaterCalculation) {
                isEnabledWaterCalculation = true;
            }
            if (deactTime == 0) deactTime = currentTime;
            if (ModConfig.tickLogInfo) {
                System.out.println("WATER: " + (isEnabledWaterCalculation ? "FANCY; " : "FAST; ") + flowUpdates + " flwUpd; " + tickDiff
                        + " tickDiff; " + forcedDeact + " deact; " + biggestRec + " max. Recurs; " + highestRecTime + " max RecTme; "
                        + (int) (ModConfig.maxTickBorder * (forcedDeact * 0.1 + 1)) + " maxDynBder; " + (int) (60 * (forcedDeact * 0.01 + 1))
                        + " minDynBder; " + (currentTime - deactTime) + " maxDeactSec; " + scheduledBlocks + " scheduled;");
            }
            biggestRec = 0;
            lastUpdatedUpdates = flowUpdates;
        }
        lastUpdatedTime = currentTime;
    }
    // ----------Other----------// }
}
