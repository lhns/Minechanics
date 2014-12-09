package org.lolhens.minechanics.core.gui;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

import org.lolhens.minechanics.Minechanics;
import org.lolhens.minechanics.core.gui.guicontainer.GuiContainerAuto;
import org.lolhens.minechanics.tools.Toolbox;
import org.lolhens.minechanics.tools.gui.container.ContainerToolbox;

public class Gui {
    private static final Map<Integer, Gui> guis = new HashMap<Integer, Gui>();
    private static int lastID = 0;

    // GUIS:
    public static final Gui toolbox = new Gui() {
        @Override
        public Container getNewContainerInstance(EntityPlayer player, World world, int x, int y, int z) {
            return new ContainerToolbox(new Toolbox.ToolboxInventory(), player.inventory);
        }
    };

    private int ID;

    public Gui() {
        ID = lastID++;
        guis.put(ID, this);
    }

    public int getID() {
        return ID;
    }

    public Container getNewContainerInstance(EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public GuiContainer getNewGuiContainerInstance(EntityPlayer player, World world, int x, int y, int z) {
        return new GuiContainerAuto(getNewContainerInstance(player, world, x, y, z));
    }

    public void open(EntityPlayer player, World world, int x, int y, int z) {
        player.openGui(Minechanics.instance, ID, world, x, y, z);
    }

    public static Gui fromID(int ID) {
        return guis.get(ID);
    }
}
