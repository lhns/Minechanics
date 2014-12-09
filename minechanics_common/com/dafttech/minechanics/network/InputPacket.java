package com.dafttech.minechanics.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;

public class InputPacket {
    private DataInputStream stream;
    private int channel;

    public InputPacket(Packet250CustomPayload packet) {
        stream = new DataInputStream(new ByteArrayInputStream(packet.data));
        channel = readInt();
    }

    public String getChannel() {
        return ChannelManager.instance.getChannel(channel);
    }

    public int readInt() {
        try {
            return stream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float readFloat() {
        try {
            return stream.readFloat();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double readDouble() {
        try {
            return stream.readDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean readBool() {
        try {
            return stream.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public NBTTagCompound readNBT() {
        try {
            short length = stream.readShort();
            if (length < 0) {
                return null;
            } else {
                byte[] data = new byte[length];
                stream.readFully(data);
                return CompressedStreamTools.decompress(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
