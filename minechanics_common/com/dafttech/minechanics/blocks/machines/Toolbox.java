package com.dafttech.minechanics.blocks.machines;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.util.Color;

import com.dafttech.minechanics.blocks.BlockMinechanics;
import com.dafttech.minechanics.client.renderer.model.Box;
import com.dafttech.minechanics.client.renderer.model.Model;
import com.dafttech.minechanics.client.renderer.model.Texture;
import com.dafttech.minechanics.util.Saved;
import com.dafttech.minechanics.util.Vector3f;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Toolbox extends BlockMinechanics {
    @Saved(false)
    private float openAngle = 0;
    @Saved
    private boolean open = false;
    @Saved
    private short dir = 0;

    public Toolbox(int blockID) {
        super(blockID);
    }

    @Override
    public void created() {

    }

    @Override
    public String getUnlocalizedName() {
        return "toolbox";
    }

    @Override
    public String[] getName() {
        return new String[] { "Toolbox" };
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.tabMisc;
    }

    @Override
    public float getHardness() {
        return 1;
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            open = !open;
            notifyTileEntityChange();
        }
        return true;
    }

    @Override
    public Model getModel(RenderMode mode, float delta) {
        RenderHelper.enableStandardItemLighting();
        final int openSpeed = 3;
        if (open && openAngle < 90) {
            openAngle += openSpeed;
            if (openAngle >= 90) notifyTileEntityChange();
        }
        if (!open && openAngle > 0) {
            openAngle -= openSpeed;
            if (openAngle <= 0) notifyTileEntityChange();
        }
        Model model = new Model().setTexture(new Texture("clean", block)).rotate(new Vector3f(0, (float) Math.toRadians(dir * 90), 0));
        Model body = new Model(0, 0, 0.2f, 1, 0.5f, 0.6f).setColor(new Color(255, 0, 0));
        model.add(body);
        body.add(new Box(0, 0, 0, 1, 1, 0.1f));
        body.add(new Box(0, 0, 0.9f, 1, 1, 0.1f));
        body.add(new Box(0, 0, 0, 0.1f, 1, 1));
        body.add(new Box(0.9f, 0, 0, 0.1f, 1, 1));
        body.add(new Box(0, 0, 0, 1, 0.1f, 1));
        Model top = new Model().setOrigin(new Vector3f(0, 0.5f, 0.8f)).rotate(new Vector3f((float) Math.toRadians(openAngle), 0, 0));
        model.add(top);
        top.add(new Box(0, 0.5f, 0.2f, 1, 0.2f, 0.6f).setColor(new Color(200, 0, 0)));
        top.add(new Box(0.45f, 0.4f, 0.1f, 0.1f, 0.2f, 0.1f).setColor(new Color(100, 100, 100)));
        Model grip = new Model(0.3f, 0.7f, 0.4f).setColor(new Color(100, 100, 100));
        top.add(grip);
        grip.add(new Box(0, 0, 0, 0.1f, 0.2f, 0.1f));
        grip.add(new Box(0.3f, 0, 0, 0.1f, 0.2f, 0.1f));
        grip.add(new Box(0, 0.1f, 0, 0.3f, 0.1f, 0.1f));
        return model;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, int side, float xOff, float yOff, float zOff) {
        return false;
    }
}
