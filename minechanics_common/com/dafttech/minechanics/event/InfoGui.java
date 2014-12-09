package com.dafttech.minechanics.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class InfoGui {
    public World world;
    public int x, y, z, guiId;
    public EntityPlayer player;
    public boolean client;

    public InfoGui(World world, int x, int y, int z, int guiId, EntityPlayer player, boolean client) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
        this.client = client;
    }
}
