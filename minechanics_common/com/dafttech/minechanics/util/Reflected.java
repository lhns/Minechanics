package com.dafttech.minechanics.util;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ThreadMinecraftServer;
import net.minecraft.util.Timer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;

public class Reflected {
    public static Reflected instance = new Reflected();

    public Field minecraftTimer = null;
    public Field theServer = null;
    public Field chunkProviderServerWorldObj = null;

    @EventListener("INITPOST")
    public void onLoad(Event event) {
        for (Field field : Minecraft.class.getDeclaredFields()) {
            if (field.getType() == Timer.class) {
                field.setAccessible(true);
                minecraftTimer = field;
            }
        }
        for (Field field : ThreadMinecraftServer.class.getDeclaredFields()) {
            if (field.getType() == MinecraftServer.class) {
                field.setAccessible(true);
                theServer = field;
            }
        }
        for (Field field : ChunkProviderServer.class.getDeclaredFields()) {
            if (field.getType() == WorldServer.class) {
                field.setAccessible(true);
                chunkProviderServerWorldObj = field;
            }
        }
    }

    public float getParTicks() {
        try {
            return ((Timer) minecraftTimer.get(Minecraft.getMinecraft())).renderPartialTicks;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public MinecraftServer getMinecraftServer(ThreadMinecraftServer thread) {
        try {
            return (MinecraftServer) theServer.get(thread);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public WorldServer getWorldServer(ChunkProviderServer chunkProviderServer) {
        try {
            return (WorldServer) chunkProviderServerWorldObj.get(chunkProviderServer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
