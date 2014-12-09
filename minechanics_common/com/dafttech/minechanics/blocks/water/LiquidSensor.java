package com.dafttech.minechanics.blocks.water;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.blocks.BlockMinechanics;
import com.dafttech.minechanics.client.renderer.model.Box;
import com.dafttech.minechanics.client.renderer.model.Model;
import com.dafttech.minechanics.client.renderer.model.Texture;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.event.Events;
import com.dafttech.minechanics.interfaces.ISmoothWaterTransition;
import com.dafttech.minechanics.util.ParticleManager;
import com.dafttech.minechanics.util.Vector3f;

public class LiquidSensor extends BlockMinechanics implements ISmoothWaterTransition {
    public LiquidSensor(int blockID) {
        super(blockID);
        Events.EVENTMANAGER.registerEventListener(this);
    }

    @Override
    public void created() {
        block.setTickRandomly(true);
    }

    @Override
    public String getUnlocalizedName() {
        return "liquidSensor";
    }

    @Override
    public String[] getName() {
        return new String[] { "Liquid Sensor" };
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.tabRedstone;
    }

    @Override
    public void onNeighborBlockChange(int neighborID) {
        getWorld().scheduleBlockUpdate(x, y, z, blockID, 10);
    }

    @Override
    public void updateTileEntity() {
    }

    @Override
    public void onBlockAdded(EntityLivingBase entityLiving, ItemStack itemStack, int side, float hitX, float hitY, float hitZ) {
        updateState(getWorld(), x, y, z);
    }

    @Override
    public void updateTick(Random random) {
        updateState(getWorld(), x, y, z);
    }

    public void updateState(World world, int x, int y, int z) {
        int highestMeta = 0;
        if (getLiquidMeta(world, x, y + 1, z) > highestMeta) highestMeta = getLiquidMeta(world, x, y + 1, z);
        if (getLiquidMeta(world, x + 1, y, z) > highestMeta) highestMeta = getLiquidMeta(world, x + 1, y, z);
        if (getLiquidMeta(world, x - 1, y, z) > highestMeta) highestMeta = getLiquidMeta(world, x - 1, y, z);
        if (getLiquidMeta(world, x, y, z + 1) > highestMeta) highestMeta = getLiquidMeta(world, x, y, z + 1);
        if (getLiquidMeta(world, x, y, z - 1) > highestMeta) highestMeta = getLiquidMeta(world, x, y, z - 1);

        int oldMeta = world.getBlockMetadata(x, y, z);
        boolean changed = false;
        if (oldMeta < highestMeta) {
            world.setBlockMetadataWithNotify(x, y, z, oldMeta + 1, ModInfo.NOTIFY_MARKBLOCK);
            changed = true;
        } else if (oldMeta > highestMeta) {
            world.setBlockMetadataWithNotify(x, y, z, highestMeta, ModInfo.NOTIFY_MARKBLOCK);
            changed = true;
        }

        int newMeta = world.getBlockMetadata(x, y, z);
        if (changed) {
            // Effect.bubble(world, x, y, z);
            if (oldMeta <= 0 && newMeta > 0 || oldMeta > 0 && newMeta <= 0) {
                world.playSoundEffect(x + 0.5D, y + 0.1D, z + 0.5D, "random.click", 0.3F, 0.6F);
            }
            world.markBlockForUpdate(x, y, z);
            world.notifyBlocksOfNeighborChange(x, y, z, blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, blockID);
            world.notifyBlocksOfNeighborChange(x + 1, y, z, blockID);
            world.notifyBlocksOfNeighborChange(x - 1, y, z, blockID);
            world.notifyBlocksOfNeighborChange(x, y, z + 1, blockID);
            world.notifyBlocksOfNeighborChange(x, y, z - 1, blockID);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            if (newMeta != highestMeta) {
                world.scheduleBlockUpdate(x, y, z, blockID, 0);
            }
        }
    }

    @Override
    public int isProvidingStrongPower(int side) {
        return iba.getBlockMetadata(x, y, z);
    }

    @Override
    public int isProvidingWeakPower(int side) {
        return iba.getBlockMetadata(x, y, z);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    public int getLiquidMeta(World world, int x, int y, int z) {
        if (!Liquid.isLiquidAtAll(world, x, y, z)) return 0;
        return world.getBlockMetadata(x, y, z) >= 8 ? 15 : (8 - (world.getBlockMetadata(x, y, z) + 1) % 9) * 2 + 1;
    }

    @Override
    public void randomDisplayTick(Random random) {
        boolean power = iba.getBlockMetadata(x, y, z) > 0;
        if (power) ParticleManager.spawnParticle(getWorld(), x + 0.5, y + 0.5, z + 0.5, 0, 0, 0, "reddust", 2);
    }

    @Override
    public Model getModel(RenderMode mode, float delta) {
        float height = (float) metadata / 15;

        Model model = new Model();
        model.add(new Box(0, 0, 0, 1, 1, 1).setTexture(new Texture("ironbars", block)).antifight());
        model.add(new Box(0, 0, 0, 1, 1, 1).setTexture(new Texture("ironbars", block)).antifight().invert());
        model.add(new Box(0, 0, 0, 1, 0.1f, 1).setTexture(new Texture("iron", block)));
        model.add(new Box(0.0625f, 0.1f, 0.0625f, 0.875f, height * 0.768 + 0.132 > 0.163 ? 0.0625f : 0.03125f, 0.875f).setTexture(new Texture("wood",
                block)));
        model.add(new Box(0, height * 0.768f + 0.132f, 0, 1, 0, 1).setTexture(new Texture("lilypad", block)));
        model.add(new Box(0.5f, 0.1f, 0, 0, height * 0.768f + 0.032f, 1).setTexture(new Texture("stringblock", block)));
        model.add(new Box(0, 0.1f, 0.5f, 1, height * 0.768f + 0.032f, 0).setTexture(new Texture("stringblock", block)));

        if (mode == RenderMode.ENTITY) {
            model.resize(new Vector3f(4, 4, 4));
            model.move(new Vector3f(-1.5f, 0, -1.5f));
        }

        return new Model(model).rotate(new Vector3f(0, (float) Math.toRadians(90 * ((x + y + z) % 4)), 0));
    }

    @Override
    public boolean isSmoothWaterTransition(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @EventListener(value = "DRAWBREAKINGANIM", filter = "blockID")
    public void onDrawBreakingAnim(Event event) {
        event.cancel();
    }

    @Override
    public float getHardness() {
        return 1;
    }

}
