package com.dafttech.minechanics.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.dafttech.eventmanager.EventManager;
import com.dafttech.minechanics.data.ModInfo;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class OutputPacket {
    private DataOutputStream stream;

    public OutputPacket(String channel) {
        stream = new DataOutputStream(new ByteArrayOutputStream());
        writeInt(ChannelManager.instance.getChannelId(channel));
    }

    private Packet250CustomPayload create() {
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = ModInfo.MOD_CHANNEL;
        packet.data = getByteStream(stream).toByteArray();
        packet.length = getByteStream(stream).size();
        return packet;
    }

    public void send() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            PacketDispatcher.sendPacketToServer(create());
        } else {
            PacketDispatcher.sendPacketToAllPlayers(create());
        }
    }

    public void send(int dimension) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            PacketDispatcher.sendPacketToServer(create());
        } else {
            PacketDispatcher.sendPacketToAllInDimension(create(), dimension);
        }
    }

    public void send(Player player) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            PacketDispatcher.sendPacketToServer(create());
        } else {
            PacketDispatcher.sendPacketToPlayer(create(), player);
        }
    }

    public void writeInt(int v) {
        try {
            stream.writeInt(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeVec3i(int x, int y, int z) {
        writeInt(x);
        writeInt(y);
        writeInt(z);
    }

    public void writeFloat(float v) {
        try {
            stream.writeFloat(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDouble(double v) {
        try {
            stream.writeDouble(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBool(boolean v) {
        try {
            stream.writeBoolean(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeNBT(NBTTagCompound v) {
        try {
            if (v == null) {
                stream.writeShort(-1);
            } else {
                byte[] data = CompressedStreamTools.compress(v);
                stream.writeShort((short) data.length);
                stream.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Field byteStreamField = getByteStreamField();

    private static Field getByteStreamField() {
        for (Field field : EventManager.getAllDeclaredFields(DataOutputStream.class))
            if (field.getType() == OutputStream.class) return field;
        return null;
    }

    private static ByteArrayOutputStream getByteStream(DataOutputStream outputStream) {
        if (outputStream != null && byteStreamField != null) {
            try {
                return (ByteArrayOutputStream) byteStreamField.get(outputStream);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
