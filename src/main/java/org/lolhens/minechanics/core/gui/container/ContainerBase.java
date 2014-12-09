package org.lolhens.minechanics.core.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container {
    protected IInventory inventory;
    protected InventoryPlayer inventoryPlayer;

    public ContainerBase(IInventory inventory, InventoryPlayer inventoryPlayer) {
        this.inventory = inventory;
        this.inventoryPlayer = inventoryPlayer;
        // the Slot constructor takes the IInventory and the slot number in that
        // it binds to
        // and the x-y coordinates it resides on-screen
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new Slot(inventory, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }

        // commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(inventoryPlayer);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public abstract boolean canInteractWith(EntityPlayer player);

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack()) return null;

        ItemStack stackInSlot = slot.getStack();
        ItemStack stack = stackInSlot.copy();

        if (slotIndex < 9) {
            if (!mergeItemStack(stackInSlot, 0, 35, true)) return null;
        } else if (!mergeItemStack(stackInSlot, 0, 9, false)) {
            return null;
        }

        if (stackInSlot.stackSize == 0) {
            slot.putStack(null);
        } else {
            slot.onSlotChanged();
        }

        if (stackInSlot.stackSize == stack.stackSize) {
            return null;
        }

        slot.onPickupFromSlot(player, stackInSlot);
        return stack;
    }
}
