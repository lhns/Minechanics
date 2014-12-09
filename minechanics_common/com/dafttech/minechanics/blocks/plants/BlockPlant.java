package com.dafttech.minechanics.blocks.plants;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventFilter;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.CommonProxy;
import com.dafttech.minechanics.client.renderer.BlockRenderer;
import com.dafttech.minechanics.client.renderer.GuiRenderer;
import com.dafttech.minechanics.client.renderer.model.Model;
import com.dafttech.minechanics.client.renderer.model.Texture;
import com.dafttech.minechanics.event.Events;
import com.dafttech.minechanics.event.InfoBoundingBox;
import com.dafttech.minechanics.event.InfoOverlay;
import com.dafttech.minechanics.util.BlockManager;
import com.dafttech.minechanics.util.Vector3f;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPlant extends BlockContainer implements ISimpleBlockRenderingHandler {
    public static InfoBoundingBox infoBoundingBox = null;

    public BlockPlant(int id) {
        super(id, Material.plants);
        // this.blockIndexInTexture = 176;
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.tabBlock);
        setHardness(0.3f);
        setStepSound(Block.soundGrassFootstep);
        setBlockBounds(0.1F, 0, 0.1F, 0.9F, 0.7F, 0.9F);
        Events.EVENTMANAGER.registerEventListener(this);
        Gene.initAll();
    }

    public String getTextureFile() {
        return CommonProxy.plants;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate() {
        return 2;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return true;
    };

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        if (world.getBlockId(x, y - 1, z) == 0) return true;
        return super.canBlockStay(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
        checkFlowerChange(par1World, par2, par3, par4);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        checkFlowerChange(par1World, par2, par3, par4);
    }

    protected final void checkFlowerChange(World par1World, int par2, int par3, int par4) {
        if (!canBlockStay(par1World, par2, par3, par4)) {
            dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (!world.isRemote) {
            TileEntityPlant te = (TileEntityPlant) world.getBlockTileEntity(x, y, z);
            int playerItem = 0;
            if (player.getCurrentEquippedItem() != null) playerItem = player.getCurrentEquippedItem().itemID;
            /* playerItem != Minechanics.item_pollen.shiftedIndex TODO */
            if (te.hasPollen && !te.isPollinated && te.state == 4 && playerItem != 0) {
                te.hasPollen = false;
                /* new ItemStack(Minechanics.item_pollen, TODO */
                ItemStack temp = new ItemStack(Item.appleRed, Gene.rnd.nextInt(2) + 1);
                temp.stackTagCompound = new NBTTagCompound();
                Genome.getChildGenomeAt(world, x, y, z).rename("genome").pushToNBT(temp.stackTagCompound);
                EntityItem spawn = new EntityItem(world);
                spawn.setPosition(x + 0.5, y + 0.5, z + 0.5);
                spawn.entityDropItem(temp, 0);
            }
            /* (playerItem == Minechanics.item_pollen.shiftedIndex) TODO */
            if (playerItem == 0) return false;
            return true;
        }
        return true;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote) {
            ((TileEntityPlant) world.getBlockTileEntity(x, y, z)).fillRandom();
            world.notifyBlockChange(x, y, z, blockID);
        }
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        dropBlockAsItem(par1World, par2, par3, par4, par5, par6);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        TileEntityPlant te = (TileEntityPlant) world.getBlockTileEntity(x, y, z);
        if (!world.isRemote && te != null) {
            ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
            /* new ItemStack(Minechanics.item_seeds, TODO */
            ItemStack temp = new ItemStack(Item.appleRed, Gene.rnd.nextInt(3) + 1);
            temp.stackTagCompound = new NBTTagCompound();
            Genome.getChildGenomeAt(world, x, y, z).rename("genome").pushToNBT(temp.stackTagCompound);
            for (Gene g : Gene._GENES) {
                ArrayList<ItemStack> add = g.getSpecialDrops(Genome.getChildGenomeAt(world, x, y, z), te.state, world, x, y, z);
                if (add.size() > 0) stacks.addAll(add);
            }
            if (te.state == 7) stacks.add(temp);
            super.breakBlock(world, x, y, z, metadata, fortune);
            world.removeBlockTileEntity(x, y, z);
            return stacks;
        }
        return new ArrayList<ItemStack>();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        new BlockRenderer(renderer, this, 0, 0, 0, 1, 1, 1, 0).render(BlockRenderer.CROSS);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        for (int i = 0; i < Genome.getGenomeAt(world, x, y, z).getRenderHeight(); i++) {
            // renderLoxodrome(x, y, z);
            new com.dafttech.minechanics.client.renderer.model.Cross().setColor(customColorMultiplier(world, x, y, z))
                    .setTexture(new Texture("grass_6", this)).render(new Vector3f(x, y, z));
        }
        return true;
    }

    public Color customColorMultiplier(IBlockAccess world, int x, int y, int z) {
        if (Genome.getGenomeAt(world, x, y, z).isBiomeColored()) {
            int var5 = 0;
            int var6 = 0;
            int var7 = 0;
            for (int var8 = -1; var8 <= 1; ++var8) {
                for (int var9 = -1; var9 <= 1; ++var9) {
                    int var10 = world.getBiomeGenForCoords(x + var9, z + var8).getBiomeGrassColor();
                    var5 += (var10 & 16711680) >> 16;
                    var6 += (var10 & 65280) >> 8;
                    var7 += var10 & 255;
                }
            }
            return new Color(var5 / 9 & 255, var6 / 9 & 255, var7 / 9 & 255);
        } else {
            return new Color(255, 255, 255);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BlockManager.getRenderId(this);
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return BlockManager.getRenderId(this);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileEntityPlant();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {

    }

    @EventFilter("blockId")
    public int filter1() {
        return blockID;
    }

    @SuppressWarnings("unused")
    @EventListener(value = "DRAWBOUNDINGBOX", filter = "blockId")
    public void onDrawBoundingBox(Event event) {
        InfoBoundingBox info = event.getInput(0, InfoBoundingBox.class);
        int height = Genome.getGenomeAt(info.world, info.x, info.y, info.z).getRenderHeight();
        float extend = ((float) Math.sin(info.player.ticksExisted / 10.0f) / 2f + 0.5f) * 0.1f + 0.05f;
        renderSelection(info.x, info.y, info.z, Genome.getGenomeAt(info.world, info.x, info.y, info.z).getRenderHeight(), info.player, info.parTicks);
        event.cancel();
    }

    @EventListener(value = "DRAWBREAKINGANIM", filter = "blockId")
    public void onDrawBreakingAnim(Event event) {
        event.cancel();
    }

    @SuppressWarnings("unused")
    @EventListener("DRAWOVERLAY")
    public void onDrawOverlay(Event event) {
        InfoOverlay info = event.getInput(0, InfoOverlay.class);
        if (infoBoundingBox != null) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            World world = infoBoundingBox.world;
            int x = infoBoundingBox.x, y = infoBoundingBox.y, z = infoBoundingBox.z;
            String humidityString = Gene.gene_Humidity.percentageFor(world, x, y, z) + "% Humidity";
            String nutritionString = Gene.gene_Nutrition.percentageFor(world, x, y, z) + "% Nutrition";
            String infoString = "This plant is growing.";
            if (!Gene.gene_Species.canGrowAt(Genome.getGenomeAt(world, x, y, z), world, x, y, z)) infoString = "This plant doesn't grow.";
            if (((TileEntityPlant) world.getBlockTileEntity(x, y, z)).state == 4
                    && !((TileEntityPlant) world.getBlockTileEntity(x, y, z)).isPollinated) infoString = "This plant is blooming.";
            if (((TileEntityPlant) world.getBlockTileEntity(x, y, z)).state == 7) infoString = "This plant is fully grown.";
            int lengthHumidityString = info.fontRenderer.getStringWidth(humidityString);
            int lengthNutritionString = info.fontRenderer.getStringWidth(nutritionString);
            int maxLength = lengthHumidityString > lengthNutritionString ? lengthHumidityString : lengthNutritionString;
            GuiRenderer.drawSimpleRect(CommonProxy.overlay_raindrop, info.scaledWidth - 18, 32, 9, 14);
            info.fontRenderer.drawString(humidityString, info.scaledWidth - (24 + lengthHumidityString), 35, 255 << 16 | 255 << 8 | 255);
            info.fontRenderer.drawString(nutritionString, info.scaledWidth - (24 + lengthNutritionString), 45, 255 << 16 | 255 << 8 | 255);
            info.fontRenderer.drawString(infoString, info.scaledWidth - (10 + info.fontRenderer.getStringWidth(infoString)), 65, 255 << 16 | 255 << 8
                    | 255);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            infoBoundingBox = null;
        }
    }

    public void renderSelection(int posX, int posY, int posZ, int size, EntityPlayer player, float parTicks) {
        float x = 0, z = 0, s = 0;
        boolean shifted = false;

        Model model = new Model();
        for (float y = 0; y <= size; y += 0.05 / ((Math.sin((float) player.ticksExisted / 120) + 2) * 10)) {
            x = (float) Math.sin((y + (float) player.ticksExisted / 120 + (shifted ? 0.2 : 0)) * Math.PI * 5)
                    / (1.8f + (float) Math.sin(y + (float) player.ticksExisted / 100) / 3 - 0.4f);
            z = (float) Math.cos((y + (float) player.ticksExisted / 120 + (shifted ? 0.2 : 0)) * Math.PI * 5)
                    / (1.8f + (float) Math.sin(y + (float) player.ticksExisted / 100) / 3 - 0.4f);
            s = ((float) Math.sin(y + (float) player.ticksExisted / 25) + 1) / 2 * 0.03f + 0.01f;

            model.add(new com.dafttech.minechanics.client.renderer.model.Box(new Vector3f(-s / 2, -s / 2, -s / 2))
                    .setSize(new Vector3f(s / 2, s / 2, s / 2)).setTexture(new Texture("green", this)).move(new Vector3f(x + 0.5F, y, z + 0.5F)));
            shifted = !shifted;
        }
        model.setBoundingBox().render(new Vector3f(posX, posY, posZ));
    }
}
