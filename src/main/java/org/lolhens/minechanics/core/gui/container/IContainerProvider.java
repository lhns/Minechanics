package org.lolhens.minechanics.core.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public interface IContainerProvider {
    public Container getNewContainerInstance(EntityPlayer player, World world, int x, int y, int z);
}
