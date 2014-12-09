package com.dafttech.minechanics.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.event.InfoPacket;
import com.dafttech.minechanics.network.ChannelManager;
import com.dafttech.minechanics.network.OutputPacket;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ParticleManager {
    public static ParticleManager instance = new ParticleManager();

    public static List<String> particles = new ArrayList<String>();
    private static float customBoundsX = 1, customBoundsY = 1, customBoundsZ = 1;

    public static void setupParticles() {
        particles.add("hugeexplosion");
        particles.add("largeexplode");
        particles.add("fireworksSpark");
        particles.add("bubble");
        particles.add("suspended");
        particles.add("depthsuspend");
        particles.add("townaura");
        particles.add("crit");
        particles.add("magicCrit");
        particles.add("smoke");
        particles.add("mobSpell");
        particles.add("mobSpellAmbient");
        particles.add("spell");
        particles.add("instantSpell");
        particles.add("witchMagic");
        particles.add("note");
        particles.add("portal");
        particles.add("enchantmenttable");
        particles.add("explode");
        particles.add("flame");
        particles.add("lava");
        particles.add("footstep");
        particles.add("splash");
        particles.add("largesmoke");
        particles.add("cloud");
        particles.add("reddust");
        particles.add("snowballpoof");
        particles.add("dripWater");
        particles.add("dripLava");
        particles.add("snowshovel");
        particles.add("slime");
        particles.add("heart");
        particles.add("angryVillager");
        particles.add("happyVillager");
        particles.add("iconcrack_");
        particles.add("tilecrack_");
    }

    public static void spawnParticle(World world, double x, double y, double z, double velX, double velY, double velZ, String particle, int count) {
        if (particles.contains(particle)) spawnParticle(world, x, y, z, velX, velY, velZ, particles.indexOf(particle), count);
    }

    public static void spawnParticle(World world, double x, double y, double z, double velX, double velY, double velZ, int particle, int count) {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.CLIENT) {
            if (count > 1) {
                for (int i = 0; i < count; i++) {
                    double tmpX = x + customBoundsX / 2 - Math.random() * customBoundsX, tmpY = y + customBoundsY / 2 - Math.random() * customBoundsY, tmpZ = z
                            + customBoundsZ / 2 - Math.random() * customBoundsZ;
                    world.spawnParticle(particles.get(particle), tmpX, tmpY, tmpZ, velX, velY, velZ);
                }
            } else {
                world.spawnParticle(particles.get(particle), x, y, z, velX, velY, velZ);
            }
        } else {
            OutputPacket packet = new OutputPacket(ModInfo.CHANNEL_PARTICLEMANAGERPARTICLES);
            packet.writeDouble(x);
            packet.writeDouble(y);
            packet.writeDouble(z);
            packet.writeDouble(velX);
            packet.writeDouble(velY);
            packet.writeDouble(velZ);
            packet.writeInt(particle);
            packet.writeInt(count);
            packet.send();
        }
    }

    public static void setCustomBounds(float x, float y, float z) {
        customBoundsX = x;
        customBoundsY = y;
        customBoundsZ = z;
        OutputPacket packet = new OutputPacket(ModInfo.CHANNEL_PARTICLEMANAGERBOUNDS);
        packet.writeFloat(x);
        packet.writeFloat(y);
        packet.writeFloat(z);
        packet.send();
    }

    public static void resetCustomBounds() {
        setCustomBounds(1, 1, 1);
    }

    @SideOnly(Side.CLIENT)
    private static void spawnParticleWithoutWorld(double x, double y, double z, double velX, double velY, double velZ, int particle, int count) {
        spawnParticle(Minecraft.getMinecraft().theWorld, x, y, z, velX, velY, velZ, particle, count);
    }

    @EventListener(value = "RECEIVEPACKET", filter = { "..data.ModInfo.channelfilter_particles", "..data.ModInfo.channelfilter_bounds" })
    public void handleParticlePacket(Event event) {
        InfoPacket info = event.getInput(0, InfoPacket.class);
        if (info.channel.equals(ModInfo.CHANNEL_PARTICLEMANAGERPARTICLES)) {
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            if (side == Side.CLIENT) {
                try {
                    spawnParticleWithoutWorld(info.inputPacket.readDouble(), info.inputPacket.readDouble(), info.inputPacket.readDouble(),
                            info.inputPacket.readDouble(), info.inputPacket.readDouble(), info.inputPacket.readDouble(), info.inputPacket.readInt(),
                            info.inputPacket.readInt());
                } catch (Exception e) {
                }
            }
        } else if (info.channel.equals(ModInfo.CHANNEL_PARTICLEMANAGERBOUNDS)) {
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            if (side == Side.CLIENT) {
                setCustomBounds(info.inputPacket.readFloat(), info.inputPacket.readFloat(), info.inputPacket.readFloat());
            }
        }
    }

    @EventListener(value = "REGISTERCHANNELS")
    public void registerChannels(Event event) {
        ChannelManager.instance.registerChannel(ModInfo.CHANNEL_PARTICLEMANAGERPARTICLES);
        ChannelManager.instance.registerChannel(ModInfo.CHANNEL_PARTICLEMANAGERBOUNDS);
    }
}
