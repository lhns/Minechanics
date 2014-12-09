package org.lolhens.minechanics.core.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

import org.lolhens.minechanics.core.gui.container.IContainerProvider;

public interface IInventoryProvider extends IContainerProvider {
    public IInventory getInventory(EntityPlayer player, World world, int x, int y, int z);
}
