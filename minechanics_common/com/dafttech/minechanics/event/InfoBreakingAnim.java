package com.dafttech.minechanics.event;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

public class InfoBreakingAnim {
    public World world;
    public int x, y, z;
    public Block block;
    public Minecraft mc;
    public RenderGlobal renderglobal;
    public Tessellator tessellator;
    public RenderBlocks renderer;
    public FontRenderer fontRenderer;
    public EntityPlayer player;
    public MovingObjectPosition movObjPos;
    public int subId;
    public ItemStack itemstack;
    public float parTicks;

    public InfoBreakingAnim(DrawBlockHighlightEvent highlightevent) {
        player = highlightevent.player;
        world = player.worldObj;
        x = highlightevent.target.blockX;
        y = highlightevent.target.blockY;
        z = highlightevent.target.blockZ;
        block = Block.blocksList[world.getBlockId(x, y, z)];
        mc = Minecraft.getMinecraft();
        fontRenderer = mc.fontRenderer;
        renderglobal = highlightevent.context;
        tessellator = Tessellator.instance;
        renderer = renderglobal.globalRenderBlocks;
        movObjPos = highlightevent.target;
        subId = highlightevent.subID;
        itemstack = highlightevent.currentItem;
        parTicks = highlightevent.partialTicks;
    }
}
