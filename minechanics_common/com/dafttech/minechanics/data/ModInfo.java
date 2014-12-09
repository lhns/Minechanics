package com.dafttech.minechanics.data;

import com.dafttech.eventmanager.EventFilter;

public class ModInfo {
    /* Debug mode */
    public static final boolean DEBUG_MODE = false;

    /* Mod specific strings */
    public static final String MOD_ID = "mcncs";
    public static final String MOD_NAME = "Minechanics";
    public static final String VERSION_NUMBER = "0.1";
    public static final String PROXY_SERVER = "com.dafttech.minechanics.CommonProxy";
    public static final String PROXY_CLIENT = "com.dafttech.minechanics.client.ClientProxy";
    public static final String MOD_CHANNEL = MOD_NAME;

    /* file paths */
    public static final String PATH_TEXTUREPREFIX = MOD_ID + ":";

    /* Block update flags (first two can be added) */
    public static final int NOTIFY_NEIGHBORS = 1;
    public static final int NOTIFY_MARKBLOCK = 2;
    public static final int NOTIFY_NOTHING = 4;

    /* Sprite numbers */
    @EventFilter("spritefilter_terrain")
    public static final int SPRITENUM_TERRAIN = 0;
    @EventFilter("spritefilter_items")
    public static final int SPRITENUM_ITEMS = 1;

    /* Package Channels */
    @EventFilter("channelfilter_particles")
    public static final String CHANNEL_PARTICLEMANAGERPARTICLES = "ParticleManager.particle";
    @EventFilter("channelfilter_bounds")
    public static final String CHANNEL_PARTICLEMANAGERBOUNDS = "ParticleManager.bounds";
    @EventFilter("channelfilter_tileentity")
    public static final String CHANNEL_TILEENTITY = "TileEntity";

    /* Mobility Flags */
    public static final int MOBILITY_PUSHABLE = 0;
    public static final int MOBILITY_BREAKABLE = 1;
    public static final int MOBILITY_LOCKED = 2;
    public static final int MOBILITY_PRESSURE = 3;

    public static final int ITEM_OFFSET = 256;
}
