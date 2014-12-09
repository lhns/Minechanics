package org.lolhens.minechanics.liquids;

import net.minecraft.world.World;

public interface IFlowingLiquidHandler {
    public boolean canReceiveLiquid(World world, int x, int y, int z, Liquid liquid, int meta, int flowDir);

    public boolean receiveLiquid(World world, int x, int y, int z, Liquid liquid, int meta, int flowDir);
}
