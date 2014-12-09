package com.dafttech.minechanics.blocks.plants;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPlant extends TileEntity {
    Genome genome = new Genome("genome", this);
    Genome child = new Genome("child", this);
    int state = 0;
    boolean hasPollen = true;
    boolean isPollinated = false;

    public TileEntityPlant() {
        onInventoryChanged();
    }

    public void fillRandom() {
        genome.fillRandom();
        child = genome.rename("child");
    }

    public void fillGenome(Genome g) {
        genome = g;
        child = g.rename("child");
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (genome.canGrowOnce(state, isPollinated, worldObj, xCoord, yCoord, zCoord)) {
                state++;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
    }

    public void pollinate(Genome with) {
        System.out.println("Pollinate!");
        child.pollinate(with);
        isPollinated = true;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("state", state);
        nbt.setBoolean("hasPollen", hasPollen);
        nbt.setBoolean("isPollinated", isPollinated);
        genome.pushToNBT(nbt);
        child.pushToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        state = nbt.getInteger("state");
        hasPollen = nbt.getBoolean("hasPollen");
        isPollinated = nbt.getBoolean("isPollinated");
        genome.pullFromNBT(nbt);
        child.pullFromNBT(nbt);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        NBTTagCompound nbt = packet.data;
        readFromNBT(nbt);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
