package com.dafttech.minechanics.util.BlockPos;

import net.minecraft.world.IBlockAccess;

public class BlockPos extends Pos {
    private IBlockAccess iba;
    private int x, y, z;

    public BlockPos(int x, int y, int z) {
        this(null, x, y, z);
    }

    public BlockPos(IBlockAccess iba, int x, int y, int z) {
        setIba(iba);
        setX(x);
        setY(y);
        setZ(z);
    }

    @Override
    public IBlockAccess getIba() {
        return iba;
    }

    @Override
    public void setIba(IBlockAccess iba) {
        this.iba = iba;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setZ(int z) {
        this.z = z;
    }
}
