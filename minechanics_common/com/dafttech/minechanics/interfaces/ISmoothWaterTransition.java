package com.dafttech.minechanics.interfaces;

import net.minecraft.world.IBlockAccess;

public interface ISmoothWaterTransition {
    public boolean isSmoothWaterTransition(IBlockAccess world, int x, int y, int z);
}
