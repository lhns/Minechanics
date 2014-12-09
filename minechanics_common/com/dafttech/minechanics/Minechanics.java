package com.dafttech.minechanics;

import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.event.Events;
import com.dafttech.minechanics.event.GameListener;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION_NUMBER)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { ModInfo.MOD_CHANNEL }, packetHandler = GameListener.class)
public class Minechanics {
    public Minechanics() {
        Events.instance.init();
        Events.EVENTMANAGER.callSync(Events.EVENT_INITCODE);
    }

    @Instance("Minechanics")
    public static Minechanics instance;

    @SidedProxy(clientSide = ModInfo.PROXY_CLIENT, serverSide = ModInfo.PROXY_SERVER)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Events.EVENTMANAGER.callSync(Events.EVENT_INITPRE, event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Events.EVENTMANAGER.callSync(Events.EVENT_INIT, event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Events.EVENTMANAGER.callSync(Events.EVENT_INITPOST, event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        Events.EVENTMANAGER.callSync(Events.EVENT_INITSERVER, event);
    }
}
