package com.dafttech.minechanics.interfaces;

import net.minecraft.world.World;

import com.dafttech.minechanics.blocks.water.Liquid;

public interface IFlowingLiquidHandler {
    public boolean canReceiveLiquid(World world, int x, int y, int z, int id, int meta, Liquid liquid);

    public boolean receiveLiquid(World world, int x, int y, int z, int id, int meta, Liquid liquid);
}
