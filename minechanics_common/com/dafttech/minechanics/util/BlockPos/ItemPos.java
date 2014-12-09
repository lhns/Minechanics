package com.dafttech.minechanics.util.BlockPos;

import net.minecraft.world.IBlockAccess;

public class ItemPos extends BlockPos {
    private int metadata;

    public ItemPos(IBlockAccess iba, int x, int y, int z, int metadata) {
        super(iba, x, y, z);
        this.metadata = metadata;
    }

    @Override
    public int getMetadata() {
        return metadata;
    }
}
