package org.lolhens.minechanics.liquids;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.world.World;

import org.lolhens.minechanics.core.config.MainConfig;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;

public class LiquidMechanics {
    static double t = 0, longest = 0;

    @EventListener("water")
    private static void waterFlow(Event event) {
        if (!MainConfig.liquid_mechanicEnabled) return;
        Block block = event.in.getType(Block.class, 0);
        Liquid liquid = Liquid.getRegisteredLiquid(block);
        if (liquid != null) {
            // System.out.println("flow"); // SLOW!!!!
            World world = event.in.getType(World.class, 0);
            int x = event.in.getType(int.class, 0);
            int y = event.in.getType(int.class, 1);
            int z = event.in.getType(int.class, 2);
            long time = System.currentTimeMillis();
            if (liquid.canTransferLiquid(world, x, y - 1, z)
            /*
             * && liquid.removeSource(world, x, y, z, false, false,
             * Config.liquid_maxSourceSearchRadius)
             */) {// System.out.println(x+" "+y+" "+z);
                liquid.transferLiquid(world, x, y - 1, z);
                // event.cancel();
                // world.scheduleBlockUpdate(x, y, z, block,
                // block.tickRate(world));
                long timeDiff = System.currentTimeMillis() - time;
                // System.out.println(timeDiff);
                t += timeDiff;
                if (longest < timeDiff) longest = timeDiff;
                if (t > 1000) {
                    t = 0;
                    longest = 0;
                    System.out.println("SEC CALC longest: " + longest);
                }
            }

        }
    }

    @EventListener("waterCountForNewSource")
    private static void waterCountForNewSource(Event event) {
        if (!MainConfig.liquid_mechanicEnabled) return;
        Block block = event.in.getFirst(Block.class);
        if (block instanceof BlockDynamicLiquid) ((BlockDynamicLiquid) block).field_149815_a = 0;
    }

    @EventListener("waterBlocksMovement")
    private static void waterBlocksMovement(Event event) {
        if (!MainConfig.liquid_mechanicEnabled) return;
        Block block = event.in.getType(Block.class, 1);
        Liquid liquid = Liquid.getRegisteredLiquid(block);
        if (liquid != null
                && liquid.isLiquid(event.in.getFirst(World.class), event.in.getType(int.class, 0),
                        event.in.getType(int.class, 1), event.in.getType(int.class, 2), Liquid.SOURCE)) event.cancel();
    }
}
