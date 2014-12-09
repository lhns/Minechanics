package com.dafttech.minechanics.items.plants;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.dafttech.minechanics.blocks.Blocks;
import com.dafttech.minechanics.blocks.plants.Gene;
import com.dafttech.minechanics.blocks.plants.Genome;
import com.dafttech.minechanics.blocks.plants.TileEntityPlant;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSeeds extends Item {
    public ItemSeeds(int par1) {
        super(par1);
        setMaxStackSize(64);
        setUnlocalizedName("seeds");
        // setIconIndex(15);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
        if (world.getBlockId(x, y + 1, z) == 0 && Genome.getGenomeFromNBT(stack.stackTagCompound, "genome").canGrowAt(world, x, y + 1, z)) {
            // TODO: Check if can be placed there
            world.setBlock(x, y + 1, z, Blocks.plant.blockID);
            ((TileEntityPlant) world.getBlockTileEntity(x, y + 1, z)).fillGenome(Genome.getGenomeFromNBT(stack.stackTagCompound, "genome"));
            stack.stackSize--;
            return true;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon("");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(ItemStack stack, int pass) {
        return Genome.getGenomeFromNBT(stack.stackTagCompound, "genome").getSeedIcon();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Phenotype: " + Gene.gene_Species.getGeneCodeByHeredity(Genome.getGenomeFromNBT(par1ItemStack.stackTagCompound, "genome")));
    }
}
