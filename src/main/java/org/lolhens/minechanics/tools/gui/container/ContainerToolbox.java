package org.lolhens.minechanics.tools.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

import org.lolhens.minechanics.core.gui.container.ContainerBase;

public class ContainerToolbox extends ContainerBase {

    public ContainerToolbox(IInventory inventory, InventoryPlayer inventoryPlayer) {
        super(inventory, inventoryPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

}
