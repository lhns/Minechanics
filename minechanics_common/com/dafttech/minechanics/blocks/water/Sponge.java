package com.dafttech.minechanics.blocks.water;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.lwjgl.util.Color;

import com.dafttech.minechanics.blocks.BlockMinechanics;
import com.dafttech.minechanics.client.renderer.model.Box;
import com.dafttech.minechanics.client.renderer.model.Model;
import com.dafttech.minechanics.client.renderer.model.Texture;
import com.dafttech.minechanics.data.ModConfig;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.interfaces.IFlowingLiquidHandler;

public class Sponge extends BlockMinechanics implements IFlowingLiquidHandler {
    private static final int squeezeReaction = Block.pistonMoving.blockID;
    private static final int squeezer = Block.pistonExtension.blockID;

    private static boolean foundWater = false;

    public Sponge(int blockID) {
        super(blockID);
    }

    @Override
    public void created() {
        block.setTickRandomly(true);
        Liquid.registerFlowingLiquidHandler(this, blockID);
    }

    @Override
    public String getUnlocalizedName() {
        return "mcncsSponge";
    }

    @Override
    public String[] getName() {
        return new String[] { "Sponge", "Wet Sponge", "Wet Sponge", "Wet Sponge", "Wet Sponge", "Wet Sponge", "Wet Sponge", "Wet Sponge",
                "Wet Sponge", "Wet Sponge", "Wet Sponge", "Wet Sponge", "Wet Sponge", "Wet Sponge", "Wet Sponge", "Ender Sponge" };
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.tabBlock;
    }

    @Override
    public float getHardness() {
        return 1;
    }

    @Override
    public Model getModel(RenderMode mode, float delta) {
        int colormultiplierR = (int) (255.0 - metadata * 11.5625f), colormultiplierG = colormultiplierR, colormultiplierB = 255;

        if (metadata == 15) {
            colormultiplierR = /* 210 */255;
            colormultiplierG = 255;
            colormultiplierB = /* 210 */255;
        }
        return new Box().setTexture(new Texture("sponge", block)).setColor(new Color(colormultiplierR, colormultiplierG, colormultiplierB));
    }

    @Override
    public boolean hasSubtypes() {
        return true;
    }

    @Override
    public void onNeighborBlockChange(int neighborID) {
        if (!ModConfig.waterMechanics) return;
        if (neighborID == squeezeReaction) {
            boolean squeezed = false;
            if (iba.getBlockId(x, y + 1, z) == squeezer && iba.getBlockMetadata(x, y + 1, z) % 8 == 0) squeezed = true;
            if (iba.getBlockId(x, y, z + 1) == squeezer && iba.getBlockMetadata(x, y, z + 1) % 8 == 2) squeezed = true;
            if (iba.getBlockId(x, y, z - 1) == squeezer && iba.getBlockMetadata(x, y, z - 1) % 8 == 3) squeezed = true;
            if (iba.getBlockId(x + 1, y, z) == squeezer && iba.getBlockMetadata(x + 1, y, z) % 8 == 4) squeezed = true;
            if (iba.getBlockId(x - 1, y, z) == squeezer && iba.getBlockMetadata(x - 1, y, z) % 8 == 5) squeezed = true;
            if (squeezed && isWet(getWorld(), x, y, z) && Liquid.LIQUID_WATER.transferLiquid(getWorld(), x, y - 1, z)) {
                delLvl(getWorld(), x, y, z);
            }

        }
        getWorld().scheduleBlockUpdate(x, y, z, blockID, tickRate());
    }

    @Override
    public void randomDisplayTick(Random rnd) {
        getWorld().scheduleBlockUpdate(x, y, z, blockID, tickRate());
        checkForEvaporate(getWorld(), x, y, z);
    }

    @Override
    public void updateTick(Random rnd) {

        if (ModConfig.waterMechanics) {
            checkForEvaporate(getWorld(), x, y, z);
            foundWater = false;
            if (Liquid.LIQUID_WATER.isLiquid(getWorld(), x, y + 1, z)) suckUpWater(getWorld(), x, y, z, x, y + 1, z);
            if (Liquid.LIQUID_WATER.isLiquid(getWorld(), x + 1, y, z)) suckUpWater(getWorld(), x, y, z, x + 1, y, z);
            if (Liquid.LIQUID_WATER.isLiquid(getWorld(), x - 1, y, z)) suckUpWater(getWorld(), x, y, z, x - 1, y, z);
            if (Liquid.LIQUID_WATER.isLiquid(getWorld(), x, y, z + 1)) suckUpWater(getWorld(), x, y, z, x, y, z + 1);
            if (Liquid.LIQUID_WATER.isLiquid(getWorld(), x, y, z - 1)) suckUpWater(getWorld(), x, y, z, x, y, z - 1);
            if (foundWater) getWorld().scheduleBlockUpdate(x, y, z, blockID, tickRate());
        }
    }

    public void checkForEvaporate(World world, int x, int y, int z) {
        if (world.rand.nextInt(3) == 0 && world.getBlockMetadata(x, y, z) > 0 && world.getBlockMetadata(x, y, z) < 15) {
            if (world.getBlockId(x, y - 1, z) == Block.fire.blockID || world.getBlockMaterial(x, y + 1, z) == Material.lava
                    || world.getBlockMaterial(x, y - 1, z) == Material.lava || world.getBlockMaterial(x + 1, y, z) == Material.lava
                    || world.getBlockMaterial(x - 1, y, z) == Material.lava || world.getBlockMaterial(x, y, z + 1) == Material.lava
                    || world.getBlockMaterial(x, y, z - 1) == Material.lava) {
                world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) - 1, ModInfo.NOTIFY_MARKBLOCK + ModInfo.NOTIFY_NEIGHBORS);
                world.markBlockForUpdate(x, y, z);

                // Effect.fizz(world, x, y, z);
            }
        }
    }

    @Override
    public void onBlockAdded(EntityLivingBase entityLiving, ItemStack itemStack, int side, float hitX, float hitY, float hitZ) {
        getWorld().scheduleBlockUpdate(x, y, z, blockID, tickRate());
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (isWet(getWorld(), x, y, z) && !isEnder(getWorld(), x, y, z) && Liquid.LIQUID_WATER.transferLiquid(getWorld(), x, y - 1, z))
            return delLvl(getWorld(), x, y, z);
        return false;
    }

    public int tickRate() {
        return 10;
    }

    public void suckUpWater(World world, int x, int y, int z, int xsuck, int ysuck, int zsuck) {
        if (Liquid.LIQUID_WATER.canTransferLiquid(world, x, y, z) && Liquid.LIQUID_WATER.removeSource(world, xsuck, ysuck, zsuck, false, false)) {
            Liquid.LIQUID_WATER.transferLiquid(world, x, y, z);
            foundWater = true;
        }
    }

    @Override
    public void getSubItems(int itemID, CreativeTabs creativeTabs, java.util.List<ItemStack> list) {
        list.add(new ItemStack(itemID, 1, 0));
        list.add(new ItemStack(itemID, 1, 14));
        list.add(new ItemStack(itemID, 1, 15));
    };

    @Override
    public boolean canReceiveLiquid(World world, int x, int y, int z, int id, int meta, Liquid liquid) {
        if (id == blockID && liquid.isLiquid("water") && (meta == 15 || meta < 14 || liquid.canTransferLiquid(world, x, y - 1, z))) return true;
        return false;
    }

    @Override
    public boolean receiveLiquid(World world, int x, int y, int z, int id, int meta, Liquid liquid) {
        if (id == blockID && liquid.isLiquid("water")) {
            if (meta == 15) {
                return true;
            } else if (meta < 14) {
                world.setBlockMetadataWithNotify(x, y, z, meta + 1, ModInfo.NOTIFY_MARKBLOCK + ModInfo.NOTIFY_NEIGHBORS);
                world.markBlockForUpdate(x, y, z);
                return true;
            } else {
                return liquid.transferLiquid(world, x, y - 1, z);
            }
        }
        return false;
    }

    public boolean isWet(World world, int x, int y, int z) {
        return world.getBlockId(x, y, z) == blockID && world.getBlockMetadata(x, y, z) > 0;
    }

    public boolean isEnder(World world, int x, int y, int z) {
        return world.getBlockId(x, y, z) == blockID && world.getBlockMetadata(x, y, z) == 15;
    }

    public boolean delLvl(World world, int x, int y, int z) {
        if (isWet(world, x, y, z)) {
            if (world.getBlockMetadata(x, y, z) != 15) {
                world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) - 1, ModInfo.NOTIFY_MARKBLOCK + ModInfo.NOTIFY_NEIGHBORS);
                world.markBlockForUpdate(x, y, z);
            }
            return true;
        }
        return false;
    }

}
