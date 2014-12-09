package org.lolhens.minechanics.tools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.lolhens.minechanics.core.gui.Gui;
import org.lolhens.minechanics.core.item.ItemBase;

public class Toolbox extends ItemBase {
    public Toolbox(String name) {
        super(name, "toolbox_0");
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        Vec3 pos = player.getPosition(1);
        int x = MathHelper.floor_double(pos.xCoord);
        int y = MathHelper.floor_double(pos.yCoord);
        int z = MathHelper.floor_double(pos.zCoord);
        Gui.toolbox.open(player, world, x, y, z);
        player.displayGUIBook(itemStack);
        return itemStack;
    }

    public static class ToolboxInventory implements IInventory {
        private ItemStack[] inv;

        public ToolboxInventory() {
            inv = new ItemStack[9];
        }

        @Override
        public int getSizeInventory() {
            return inv.length;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return inv[slot];
        }

        @Override
        public void setInventorySlotContents(int slot, ItemStack stack) {
            inv[slot] = stack;
            if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                stack.stackSize = getInventoryStackLimit();
            }
        }

        @Override
        public ItemStack decrStackSize(int slot, int amt) {
            ItemStack stack = getStackInSlot(slot);
            if (stack != null) {
                if (stack.stackSize <= amt) {
                    setInventorySlotContents(slot, null);
                } else {
                    stack = stack.splitStack(amt);
                    if (stack.stackSize == 0) {
                        setInventorySlotContents(slot, null);
                    }
                }
            }
            return stack;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int slot) {
            ItemStack stack = getStackInSlot(slot);
            if (stack != null) {
                setInventorySlotContents(slot, null);
            }
            return stack;
        }

        @Override
        public int getInventoryStackLimit() {
            return 64;
        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer player) {
            return true;
        }

        @Override
        public String getInventoryName() {
            return "ToolboxInv";
        }

        @Override
        public boolean hasCustomInventoryName() {
            return false;
        }

        @Override
        public void markDirty() {
        }

        @Override
        public void openInventory() {
        }

        @Override
        public void closeInventory() {
        }

        @Override
        public boolean isItemValidForSlot(int var1, ItemStack var2) {
            return false;
        }
    }
}
