package org.lolhens.minechanics;

import static org.lolhens.minechanics.core.reference.Package.CONFGUI_FACTORY;
import static org.lolhens.minechanics.core.reference.Package.LOLHENS;
import static org.lolhens.minechanics.core.reference.Package.PROXY_CLIENT;
import static org.lolhens.minechanics.core.reference.Package.PROXY_SERVER;
import static org.lolhens.minechanics.core.reference.Reference.MOD_ID;
import static org.lolhens.minechanics.core.reference.Reference.MOD_NAME;
import static org.lolhens.minechanics.core.reference.Reference.VERSION;

import java.io.IOException;

import org.lolhens.minechanics.core.config.Configurator;
import org.lolhens.minechanics.core.config.MainConfig;
import org.lolhens.minechanics.core.event.Events;
import org.lolhens.minechanics.core.gui.GuiHandler;
import org.lolhens.minechanics.core.proxy.CommonProxy;

import com.dafttech.classfile.URLClassLocation;
import com.dafttech.eventmanager.EventType;
import com.dafttech.reflect.ReflectionUtil;
import com.dafttech.storage.filterlist.Blacklist;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, canBeDeactivated = false, guiFactory = CONFGUI_FACTORY)
public class Minechanics {
    @Mod.Instance(MOD_ID)
    public static Minechanics instance;

    @SidedProxy(clientSide = PROXY_CLIENT, serverSide = PROXY_SERVER)
    public static CommonProxy proxy;

    public Configurator configurator;

    @Mod.EventHandler
    public void initPre(FMLPreInitializationEvent event) throws ClassNotFoundException, IOException {
        FMLCommonHandler.instance().bus().register(instance);
        registerEventListeners();
        configurator = new Configurator(event, MainConfig.class);
        configurator.synch();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        Events.eventManager.callSync(Events.initPre, event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Events.eventManager.callSync(Events.init, event);
    }

    @Mod.EventHandler
    public void initPost(FMLPostInitializationEvent event) {
        Events.eventManager.callSync(Events.initPost, event);
    }

    @Mod.EventHandler
    public void initServer(FMLServerStartingEvent event) {
        Events.eventManager.callSync(Events.initServer, event);
    }

    @Mod.EventHandler
    public void msgIMC(FMLInterModComms.IMCEvent event) {
        Events.eventManager.callSync(Events.msgIMC, event);
    }

    protected static void registerEventListeners() throws ClassNotFoundException, IOException {
        Class<?> loadedClass;
        Object instance;
        for (URLClassLocation ucl : new URLClassLocation(Minechanics.class).discoverSourceURL()) {
            if (ucl.getQualifiedName().toLowerCase().startsWith(LOLHENS)) {
                loadedClass = ucl.loadClass(Minechanics.class.getClassLoader());
                System.out.println(loadedClass);
                instance = ReflectionUtil.getSingletonInstance(loadedClass);
                if (instance == null) instance = loadedClass;
                Events.eventManager.registerEventListener(instance, new Blacklist<EventType>(Events.transform));
            }
        }
    }
}
