package com.dafttech.minechanics.blocks;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.network.ChannelManager;
import com.dafttech.minechanics.util.BlockPos.Pos;
import com.dafttech.minechanics.util.BlockPos.TileEntityPos;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class TileEntityUniversal extends TileEntity {
    protected BlockMinechanics blockMcncs = null;
    public Pos pos;

    private DataNBTContainer dataNbt = null;
    private boolean clientLoaded = false;
    protected boolean synching = false;

    public TileEntityUniversal() {
        pos = new TileEntityPos(this);
    }

    public BlockMinechanics getBlockMcncs() {
        if (blockMcncs == null) {
            if (worldObj != null && getBlockType() instanceof BlockUniversal) {
                blockMcncs = ((BlockUniversal) getBlockType()).blockMcncs.setIgnoreTileEntity().getNewInstance((BlockUniversal) getBlockType(),
                        blockMetadata, worldObj, xCoord, yCoord, zCoord);
            }
        }
        return blockMcncs;
    }

    private void saveFields(NBTTagCompound nbt, boolean synched) {
        for (Field field : blockMcncs.getSavedFields(synched)) {
            try {
                writeObjToNBT(nbt, field.getName(), field.getType(), field.get(blockMcncs));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void writeObjToNBT(NBTTagCompound nbt, String name, Class<?> type, Object data) {
        if (data == null || name == null || nbt == null) return;
        if (type.isArray()) {
            NBTTagCompound dataNbt = new NBTTagCompound();
            dataNbt.setInteger("size", Array.getLength(data));
            for (int i = 0; i < Array.getLength(data); i++) {
                writeObjToNBT(dataNbt, String.valueOf(i), type.getComponentType(), Array.get(data, i));
            }
            nbt.setCompoundTag(name, dataNbt);
        } else if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            nbt.setBoolean(name, (boolean) data);
        } else if (byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
            nbt.setByte(name, (byte) data);
        } else if (short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
            nbt.setShort(name, (short) data);
        } else if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
            nbt.setInteger(name, (int) data);
        } else if (long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
            nbt.setLong(name, (long) data);
        } else if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
            nbt.setFloat(name, (float) data);
        } else if (double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
            nbt.setDouble(name, (double) data);
        } else if (String.class.isAssignableFrom(type)) {
            nbt.setString(name, (String) data);
        } else if (ItemStack.class.isAssignableFrom(type)) {
            NBTTagCompound dataNbt = new NBTTagCompound();
            ((ItemStack) data).writeToNBT(dataNbt);
            nbt.setCompoundTag(name, dataNbt);
        }
    }

    private void loadFields(NBTTagCompound nbt, boolean synched) {
        for (Field field : blockMcncs.getSavedFields(synched)) {
            try {
                field.set(blockMcncs, readObjFromNBT(nbt, field.getName(), field.getType()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Object readObjFromNBT(NBTTagCompound nbt, String name, Class<?> type) {
        if (name == null || nbt == null) return null;
        if (type.isArray()) {
            NBTTagCompound dataNbt = nbt.getCompoundTag(name);
            Object objects = Array.newInstance(type.getComponentType(), dataNbt.getInteger("size"));
            for (int i = 0; i < Array.getLength(objects); i++) {
                Array.set(objects, i, readObjFromNBT(dataNbt, String.valueOf(i), type.getComponentType()));
            }
            return type.cast(objects);
        } else if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            return nbt.getBoolean(name);
        } else if (byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
            return nbt.getByte(name);
        } else if (short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
            return nbt.getShort(name);
        } else if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
            return nbt.getInteger(name);
        } else if (long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
            return nbt.getLong(name);
        } else if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
            return nbt.getFloat(name);
        } else if (double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
            return nbt.getDouble(name);
        } else if (String.class.isAssignableFrom(type)) {
            return nbt.getString(name);
        } else if (ItemStack.class.isAssignableFrom(type)) {
            return ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(name));
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        dataNbt = new DataNBTContainer(nbt.getCompoundTag("data"), synching);
        synching = false;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (getBlockMcncs() != null) {
            NBTTagCompound dataNbt = new NBTTagCompound();
            saveFields(dataNbt, synching);
            nbt.setCompoundTag("data", dataNbt);
        }
        synching = false;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses,
     * e.g. the mob spawner uses this to count ticks and creates a new spawn
     * inside its implementation.
     */
    @Override
    public void updateEntity() {
        super.updateEntity();
        if (getBlockMcncs() != null) {
            if (dataNbt != null) {
                loadFields(dataNbt.nbt, dataNbt.synched);
                dataNbt = null;
                clientLoaded = true;
                blockMcncs.onTileEntityLoaded(this);
            } else if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && !clientLoaded) {
                blockMcncs.requestTileEntity();
                clientLoaded = true;
            }
            blockMcncs.updateTileEntity();
        }
    }

    /**
     * Overriden in a sign to provide the text.
     */
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbttagcompound);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {

    }

    @EventListener(value = "REGISTERCHANNELS")
    public static void registerChannels(Event event) {
        ChannelManager.instance.registerChannel(ModInfo.CHANNEL_TILEENTITY);
    }

    private static class DataNBTContainer {
        public NBTTagCompound nbt;
        public boolean synched;

        public DataNBTContainer(NBTTagCompound nbt, boolean synched) {
            this.nbt = nbt;
            this.synched = synched;
        }
    }
}
