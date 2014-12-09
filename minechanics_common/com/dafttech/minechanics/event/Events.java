package com.dafttech.minechanics.event;

import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventManager;
import com.dafttech.eventmanager.EventType;
import com.dafttech.minechanics.blocks.Blocks;
import com.dafttech.minechanics.blocks.TileEntityUniversal;
import com.dafttech.minechanics.blocks.water.Liquid;
import com.dafttech.minechanics.client.Textures;
import com.dafttech.minechanics.data.ModConfig;
import com.dafttech.minechanics.items.Items;
import com.dafttech.minechanics.network.ChannelManager;
import com.dafttech.minechanics.network.StandardCommandHandler;
import com.dafttech.minechanics.util.ParticleManager;
import com.dafttech.minechanics.util.Reflected;

public class Events {
    public static Events instance = new Events();

    public static final EventManager EVENTMANAGER = new EventManager();

    public static final EventType EVENT_DRAWBREAKINGANIM = new EventType(EVENTMANAGER, "DRAWBREAKINGANIM") {
        @Override
        public boolean applyFilter(Event event, Object[] filter, Object eventlistener) {
            return event.getInput(0, InfoBreakingAnim.class).block.blockID == (Block) filter[0];
        }
    };

    public static final EventType EVENT_DRAWBOUNDINGBOX = new EventType(EVENTMANAGER, "DRAWBOUNDINGBOX") {
        @Override
        public boolean applyFilter(Event event, Object[] filter, Object eventlistener) {
            return event.getInput(0, InfoBoundingBox.class).block.blockID == (Integer) filter[0];
        }
    };

    public static final EventType EVENT_DRAWGUI = new EventType(EVENTMANAGER, "DRAWGUI") {
        @Override
        public boolean applyFilter(Event event, Object[] filter, Object eventlistener) {
            return event.getInput(0, InfoGui.class).guiId == (Integer) filter[0];
        }
    };

    public static final EventType EVENT_DRAWOVERLAY = new EventType(EVENTMANAGER, "DRAWOVERLAY");

    public static final EventType EVENT_RECEIVEPACKET = new EventType(EVENTMANAGER, "RECEIVEPACKET") {
        @Override
        public boolean applyFilter(Event event, Object[] filter, Object eventlistener) {
            return event.getInput(0, InfoPacket.class).channel.equals(filter[0]);
        }
    };

    public static final EventType EVENT_RECEIVECOMMAND = new EventType(EVENTMANAGER, "RECEIVECOMMAND");

    public static final EventType EVENT_WORLDTICK = new EventType(EVENTMANAGER, "WORLDTICK");

    public static final EventType EVENT_INITCODE = new EventType(EVENTMANAGER, "INITCODE");

    public static final EventType EVENT_INITPRE = new EventType(EVENTMANAGER, "INITPRE");

    public static final EventType EVENT_INIT = new EventType(EVENTMANAGER, "INIT");

    public static final EventType EVENT_INITPOST = new EventType(EVENTMANAGER, "INITPOST");

    public static final EventType EVENT_INITSERVER = new EventType(EVENTMANAGER, "INITSERVER");

    public static final EventType EVENT_ITEMTOSS = new EventType(EVENTMANAGER, "ITEMTOSS") {
        @Override
        public boolean applyFilter(Event event, Object[] filter, Object eventlistener) {
            return event.getInput(0, ItemTossEvent.class).entityItem.getEntityItem().itemID == (Integer) filter[0];
        }
    };

    public static final EventType EVENT_ITEMEXPIRE = new EventType(EVENTMANAGER, "ITEMEXPIRE") {
        @Override
        public boolean applyFilter(Event event, Object[] filter, Object eventlistener) {
            return event.getInput(0, ItemExpireEvent.class).entityItem.getEntityItem().itemID == (Integer) filter[0];
        }
    };

    public static final EventType EVENT_ITEMPICKUP = new EventType(EVENTMANAGER, "ITEMPICKUP") {
        @Override
        public boolean applyFilter(Event event, Object[] filter, Object eventlistener) {
            return event.getInput(0, EntityItemPickupEvent.class).item.getEntityItem().itemID == (Integer) filter[0];
        }
    };

    public static final EventType EVENT_TEXTURESTITCH = new EventType(EVENTMANAGER, "TEXTURESTITCH");

    public static final EventType EVENT_TEXTUREREGISTER = new EventType(EVENTMANAGER, "TEXTUREREGISTER") {
        @Override
        protected boolean applyFilter(Event event, Object[] filter, Object eventListener) {
            return event.getInput(1, Integer.class) == (int) filter[0];
        }
    };

    public static final EventType EVENT_REGISTERCHANNELS = new EventType(EVENTMANAGER, "REGISTERCHANNELS");

    public void init() {
        EVENTMANAGER.registerEventListener(ChannelManager.instance);
        EVENTMANAGER.registerEventListener(ModConfig.instance);
        EVENTMANAGER.registerEventListener(Blocks.instance);
        EVENTMANAGER.registerEventListener(Items.instance);
        EVENTMANAGER.registerEventListener(Liquid.instance);
        EVENTMANAGER.registerEventListener(Textures.instance);
        EVENTMANAGER.registerEventListener(GameListener.instance);
        EVENTMANAGER.registerEventListener(ParticleManager.instance);
        EVENTMANAGER.registerEventListener(StandardCommandHandler.instance);
        EVENTMANAGER.registerEventListener(Reflected.instance);
        EVENTMANAGER.registerEventListener(TileEntityUniversal.class);
    }
}
