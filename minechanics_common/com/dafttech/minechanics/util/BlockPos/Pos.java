package com.dafttech.minechanics.util.BlockPos;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.dafttech.minechanics.blocks.TileEntityUniversal;

public abstract class Pos {

    public World getWorld() {
        IBlockAccess iba = getIba();
        if (iba instanceof World) return (World) iba;
        return null;
    }

    public int getBlockID() {
        return getIba().getBlockId(getX(), getY(), getZ());
    }

    public Block getBlock() {
        int myID = getBlockID();
        if (myID < Block.blocksList.length) return Block.blocksList[myID];
        return null;
    }

    public int getMetadata() {
        return getIba().getBlockMetadata(getX(), getY(), getZ());
    }

    public TileEntity getTileEntity() {
        return getIba().getBlockTileEntity(getX(), getY(), getZ());
    }

    public TileEntityUniversal getTileEntityUniversal() {
        TileEntity myTile = getTileEntity();
        if (myTile instanceof TileEntityUniversal) return (TileEntityUniversal) myTile;
        return null;
    }

    public abstract IBlockAccess getIba();

    public abstract void setIba(IBlockAccess iba);

    public abstract int getX();

    public abstract void setX(int x);

    public abstract int getY();

    public abstract void setY(int y);

    public abstract int getZ();

    public abstract void setZ(int z);
}
