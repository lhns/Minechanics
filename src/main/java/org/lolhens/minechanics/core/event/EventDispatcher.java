package org.lolhens.minechanics.core.event;

import java.util.Iterator;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import com.dafttech.eventmanager.EventListener;
import com.dafttech.reflect.ReflectionUtil.SingletonInstance;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EventDispatcher {
    @SingletonInstance
    public static EventDispatcher instance = new EventDispatcher();

    @EventListener(value = "init.pre", priority = 101)
    private void init() {
        FMLCommonHandler.instance().bus().register(instance);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void initTexturesPre(TextureStitchEvent.Pre event) {
        Events.eventManager.callSync(Events.initTexturesPre, event);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void initTexturesPost(TextureStitchEvent.Post event) {
        Events.eventManager.callSync(Events.initTexturesPost, event);
    }

    @SubscribeEvent
    public void tick(TickEvent event) {
        Events.eventManager.callSync(Events.tick, event);
    }

    // TODO: NOT WORKING!
    @SubscribeEvent
    public void onDrawHighlight(DrawBlockHighlightEvent event) {
        if (event.target.entityHit == null) {
            Iterator<?> iDamagedBlocks;
            try {
                iDamagedBlocks = event.context.damagedBlocks.values().iterator();
                while (iDamagedBlocks.hasNext()) {
                    DestroyBlockProgress progress = (DestroyBlockProgress) iDamagedBlocks.next();
                    if (progress.getPartialBlockX() == event.target.blockX && progress.getPartialBlockZ() == event.target.blockZ
                            && progress.getPartialBlockY() == event.target.blockY
                            && Events.eventManager.callSync(Events.drawBreakingAnim, event).isCancelled())
                        iDamagedBlocks.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Events.eventManager.callSync(Events.drawBoundingBox, event).isCancelled()) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void itemToss(ItemTossEvent event) {
        Events.eventManager.callSync(Events.itemToss, event);
    }

    @SubscribeEvent
    public void itemPickup(EntityItemPickupEvent event) {
        Events.eventManager.callSync(Events.itemPickup, event);
    }

    @SubscribeEvent
    public void itemExpire(ItemExpireEvent event) {
        Events.eventManager.callSync(Events.itemExpire, event);
    }

}
