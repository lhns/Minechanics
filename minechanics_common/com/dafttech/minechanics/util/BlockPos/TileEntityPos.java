package com.dafttech.minechanics.util.BlockPos;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileEntityPos extends Pos {
    private TileEntity tileEntity;

    public TileEntityPos(TileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public IBlockAccess getIba() {
        return tileEntity.worldObj;
    }

    @Override
    public int getX() {
        return tileEntity.xCoord;
    }

    @Override
    public int getY() {
        return tileEntity.yCoord;
    }

    @Override
    public int getZ() {
        return tileEntity.zCoord;
    }

    @Override
    public void setIba(IBlockAccess iba) {
        if (iba instanceof World) tileEntity.worldObj = (World) iba;
    }

    @Override
    public void setX(int x) {
        tileEntity.xCoord = x;
    }

    @Override
    public void setY(int y) {
        tileEntity.yCoord = y;
    }

    @Override
    public void setZ(int z) {
        tileEntity.zCoord = z;
    }
}
