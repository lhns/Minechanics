package com.dafttech.minechanics.event;

import java.util.EnumSet;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import org.lwjgl.opengl.GL11;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.Minechanics;
import com.dafttech.minechanics.data.ModInfo;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class GameListener implements ITickHandler, IPacketHandler, IGuiHandler {
    public static GameListener instance = new GameListener();

    public static void openGui(EntityPlayer player, int x, int y, int z, int modGuiId) {
        FMLNetworkHandler.openGui(player, Minechanics.instance, modGuiId, player.worldObj, x, y, z);
    }

    @EventListener("INITCODE")
    public void onInitCode(Event event) {
        MinecraftForge.EVENT_BUS.register(instance);
    }

    @EventListener("INITPRE")
    public void onInitPre(Event event) {
        TickRegistry.registerTickHandler(instance, Side.SERVER);
        TickRegistry.registerTickHandler(instance, Side.CLIENT);
        NetworkRegistry.instance().registerGuiHandler(Minechanics.instance, instance);
    }

    @EventListener("INITSERVER")
    public void onInitServer(Event event) {
        ((ServerCommandManager) MinecraftServer.getServer().getCommandManager()).registerCommand(new CommandMinechanics());
    }

    // ----------[ Listening - BoundingBox ]----------
    // ----------[ Listening - BreakingAnim ]----------
    @ForgeSubscribe
    public void onDrawHighlight(DrawBlockHighlightEvent highlightevent) {
        if (highlightevent.target.entityHit == null) {
            InfoBoundingBox info = new InfoBoundingBox(highlightevent);
            Iterator<?> damagedBlocks = info.renderglobal.damagedBlocks.values().iterator();
            while (damagedBlocks.hasNext()) {
                DestroyBlockProgress progress = (DestroyBlockProgress) damagedBlocks.next();
                if (progress.getPartialBlockX() == info.x && progress.getPartialBlockZ() == info.z && progress.getPartialBlockY() == info.y
                        && Events.EVENTMANAGER.callSync(Events.EVENT_DRAWBREAKINGANIM, info).isCancelled()) damagedBlocks.remove();
            }
            if (Events.EVENTMANAGER.callSync(Events.EVENT_DRAWBOUNDINGBOX, (InfoBreakingAnim) info).isCancelled()) highlightevent.setCanceled(true);
        }
    }

    @ForgeSubscribe
    public void onItemToss(ItemTossEvent event) {
        Events.EVENTMANAGER.callSync(Events.EVENT_ITEMTOSS, event);
    }

    @ForgeSubscribe
    public void onItemExpire(ItemExpireEvent event) {
        Events.EVENTMANAGER.callSync(Events.EVENT_ITEMEXPIRE, event);
    }

    @ForgeSubscribe
    public void onItemPickup(EntityItemPickupEvent event) {
        Events.EVENTMANAGER.callSync(Events.EVENT_ITEMPICKUP, event);
    }

    @ForgeSubscribe
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        Events.EVENTMANAGER.callSync(Events.EVENT_TEXTURESTITCH, (TextureStitchEvent) event);
    }

    // ----------[ Listening - Gui ]----------
    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
        Event event = Events.EVENTMANAGER.callSync(Events.EVENT_DRAWGUI, new InfoGui(world, x, y, z, guiId, player, false));
        if (event.getOutput().size() > 0) return event.getOutput().get(0);
        return null;
    }

    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
        Event event = Events.EVENTMANAGER.callSync(Events.EVENT_DRAWGUI, new InfoGui(world, x, y, z, guiId, player, true));
        if (event.getCleanOutput().size() > 0) return event.getCleanOutput().get(0);
        return null;
    }

    // ----------[ Listening - Tick ]----------
    // ----------[ Listening - Overlay ]----------
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        if (type.contains(TickType.WORLD)) Events.EVENTMANAGER.callSync(Events.EVENT_WORLDTICK, new InfoTick());
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && type.contains(TickType.RENDER)) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            if (!mc.skipRenderWorld && mc.theWorld != null && (!mc.gameSettings.hideGUI || mc.currentScreen != null)) {
                mc.entityRenderer.setupOverlayRendering();
                GL11.glEnable(GL11.GL_BLEND);
                Events.EVENTMANAGER.callSync(Events.EVENT_DRAWOVERLAY, new InfoOverlay(mc, scaledResolution));
                GL11.glDisable(GL11.GL_LIGHTING);
            }
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.RENDER, TickType.WORLD);
    }

    @Override
    public String getLabel() {
        return ModInfo.MOD_ID + "_CoreListener";
    }

    // ----------[ Listening - Packet ]----------
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        Events.EVENTMANAGER.callSync(Events.EVENT_RECEIVEPACKET, new InfoPacket(manager, packet, player));
    }

    // ----------[ Listening - Command ]----------
    private static class CommandMinechanics extends CommandBase {
        @Override
        public String getCommandName() {
            return "minechanics";
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 2;
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return null;// sender.translateString("commands.minechanics.usage",
                        // new Object[0]);
        }

        @Override
        public void processCommand(ICommandSender sender, String[] command) {
            if (command.length > 0) {
                for (int i = 0; i < command.length; i++) {
                    command[i] = command[i].toLowerCase();
                }
                Event event = Events.EVENTMANAGER.callSync(Events.EVENT_RECEIVECOMMAND, new InfoCommand(sender, command));
                for (Object adminMsg : event.getCleanOutput()) {
                    notifyAdmins(sender, (String) adminMsg, new Object[] {});
                }
                return;
            }
            throw new WrongUsageException("commands.minechanics.usage", new Object[0]);
        }
    }
}
