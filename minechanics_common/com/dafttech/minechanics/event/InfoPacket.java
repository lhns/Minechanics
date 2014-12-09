package com.dafttech.minechanics.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import com.dafttech.minechanics.network.InputPacket;

import cpw.mods.fml.common.network.Player;

public class InfoPacket {
    public INetworkManager manager;
    public Packet250CustomPayload packet;
    public String channel;
    public InputPacket inputPacket;
    public Player networkPlayer;
    public EntityPlayer player;
    public World world;

    public InfoPacket(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        this.manager = manager;
        this.packet = packet;
        networkPlayer = player;
        this.player = (EntityPlayer) networkPlayer;
        world = this.player.worldObj;
        inputPacket = new InputPacket(packet);
        channel = inputPacket.getChannel();
    }
}
