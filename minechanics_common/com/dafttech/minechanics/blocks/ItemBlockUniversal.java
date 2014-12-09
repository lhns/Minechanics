package com.dafttech.minechanics.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemBlockUniversal extends ItemBlock {
    public ItemBlockUniversal(int itemID) {
        super(itemID);
        setHasSubtypes(getBlockMcncs().hasSubtypes());
        setUnlocalizedName(getBlockMcncs().getUnlocalizedName());
        String[] names = getBlockMcncs().getName();
        LanguageRegistry.addName(this, getBlockMcncs().getUnlocalizedName());
        for (int i = 0; i < 16; i++) {
            LanguageRegistry.addName(new ItemStack(this, 1, i), i > names.length - 1 ? names[names.length - 1] : names[i]);
        }
    }

    private BlockMinechanics getBlockMcncs() {
        return ((BlockUniversal) Block.blocksList[getBlockID()]).blockMcncs;
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        if (itemStack == null) return getBlockMcncs().getUnlocalizedName();
        return getBlockMcncs().getUnlocalizedName() + "." + itemStack.getItemDamage();
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubItems(int itemID, CreativeTabs creativeTabs, List list) {
        getBlockMcncs().getSubItems(itemID, creativeTabs, list);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOff, float yOff, float zOff) {
        if (getBlockMcncs().getNewInstance((BlockUniversal) Block.blocksList[((ItemBlock) stack.getItem()).getBlockID()], stack.getItemDamage(),
                world, x, y, z).onItemUse(stack, player, side, xOff, yOff, zOff)) {
            return true;
        } else {
            return super.onItemUse(stack, player, world, x, y, z, side, xOff, yOff, zOff);
        }
    }
}
