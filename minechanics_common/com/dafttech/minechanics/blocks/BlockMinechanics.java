package com.dafttech.minechanics.blocks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventFilter;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.eventmanager.EventManager;
import com.dafttech.minechanics.client.renderer.model.Model;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.event.InfoPacket;
import com.dafttech.minechanics.network.InputPacket;
import com.dafttech.minechanics.network.OutputPacket;
import com.dafttech.minechanics.util.Saved;
import com.dafttech.minechanics.util.Vector3f;
import com.dafttech.minechanics.util.BlockPos.Pos;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public abstract class BlockMinechanics {
    @EventFilter("blockID")
    public int blockID;

    public static enum RenderMode {
        WORLD, ENTITY, EQUIPPED, EQUIPPED_FIRST_PERSON, INVENTORY, FIRST_PERSON_MAP
    }

    public BlockMinechanics(int blockID) {
        this.blockID = blockID;
    }

    public BlockUniversal register() {
        return new BlockUniversal(this);
    }

    public BlockMinechanics getInstance(Pos pos) {
        if (pos != null && hasTileEntity()) {
            TileEntityUniversal tileEntity = pos.getTileEntityUniversal();
            if (tileEntity != null) return tileEntity.blockMcncs;
        }
        return this;
    }

    protected List<Field> getSavedFields(boolean synching) {
        List<Field> fields = new ArrayList<Field>();
        for (Field field : EventManager.getAnnotatedFields(getClass(), Saved.class, false, null)) {
            if (!synching || field.getAnnotation(Saved.class).value()) fields.add(field);
        }
        return fields;
    }

    public BlockUniversal getBlock() {
        if (Block.blocksList[blockID] instanceof BlockUniversal) return (BlockUniversal) Block.blocksList[blockID];
        return null;
    }

    public int getBlockID() {
        return blockID;
    }

    public static TileEntityUniversal getTileEntity(IBlockAccess iba, int x, int y, int z) {
        if (iba != null && iba.getBlockTileEntity(x, y, z) instanceof TileEntityUniversal)
            return (TileEntityUniversal) iba.getBlockTileEntity(x, y, z);
        return null;
    }

    public void render(Pos pos, RenderMode mode, RenderBlocks renderer, float delta) {
        Model model = new Model(getModel(mode, delta));
        if (mode == RenderMode.WORLD || mode == RenderMode.ENTITY) {
            model.setBrightness(pos.getIba(), pos.getX(), pos.getY(), pos.getZ());
        }
        if (mode == RenderMode.WORLD) {
            model.move(new Vector3f(pos.getX(), pos.getY(), pos.getZ()));
        } else if (mode == RenderMode.ENTITY) {
            model.move(new Vector3f(-.5f, -.5f, -.5f));
        } else if (mode == RenderMode.INVENTORY) {
            model.move(new Vector3f(0, -0.1f, 0));
        }
        model.render();
    }

    public void notifyTileEntityChange(Pos pos) {
        notifyTileEntityChange(pos, FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER);
    }

    private void notifyTileEntityChange(Pos pos, boolean synching) {
        TileEntityUniversal tileEntity = getTileEntity(pos.getIba(), pos.getX(), pos.getY(), pos.getZ());
        if (tileEntity != null) {
            OutputPacket packet = new OutputPacket(ModInfo.CHANNEL_TILEENTITY);
            packet.writeVec3i(pos.getX(), pos.getY(), pos.getZ());
            packet.writeBool(false); // request
            packet.writeBool(synching); // synched
            NBTTagCompound nbt = new NBTTagCompound();
            tileEntity.synching = synching;
            tileEntity.writeToNBT(nbt);
            packet.writeNBT(nbt);
            packet.send();
        }
    }

    protected void requestTileEntity(Pos pos) {
        TileEntityUniversal tileEntity = getTileEntity(pos.getIba(), pos.getX(), pos.getY(), pos.getZ());
        if (tileEntity != null) {
            OutputPacket packet = new OutputPacket(ModInfo.CHANNEL_TILEENTITY);
            packet.writeVec3i(pos.getX(), pos.getY(), pos.getZ());
            packet.writeBool(true); // request
            packet.send();
        }
    }

    @EventListener(value = "RECEIVEPACKET", filter = ModInfo.CHANNEL_TILEENTITY)
    private static void receiveTileEntityData(Event event) {
        InfoPacket infoPacket = event.getInput(0, InfoPacket.class);
        InputPacket packet = infoPacket.inputPacket;
        TileEntityUniversal tileEntity = getTileEntity(infoPacket.world, packet.readInt(), packet.readInt(), packet.readInt());
        if (tileEntity != null) {
            boolean request = packet.readBool();
            if (request) {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && tileEntity.blockMcncs != null) {
                    tileEntity.blockMcncs.notifyTileEntityChange(tileEntity.pos, false);
                }
            } else {
                boolean synching = packet.readBool();
                NBTTagCompound nbt = packet.readNBT();
                tileEntity.synching = synching;
                tileEntity.readFromNBT(nbt);
                infoPacket.world.markBlockForUpdate(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            }
        }
    }

    // wrapped methods

    public abstract void created();

    public abstract String getUnlocalizedName();

    public abstract String[] getName();

    public abstract CreativeTabs getCreativeTab();

    public abstract float getHardness();

    public Material getMaterial() {
        return Material.ground;
    }

    public void setBlockBoundsBasedOnState() {
    }

    public void onEntityCollidedWithBlock(Entity entity) {
    }

    public Model getModel(RenderMode mode, float delta) {
        return null;
    }

    public boolean hasSubtypes() {
        return false;
    }

    public boolean hasTileEntity() {
        return getSavedFields(false).size() > 0;
    }

    public void onTileEntityLoaded(TileEntityUniversal tileEntity) {

    }

    public void onBlockAdded(EntityLivingBase entityLiving, ItemStack itemStack, int side, float hitX, float hitY, float hitZ) {

    }

    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, int side, float xOff, float yOff, float zOff) {
        return false;
    }

    public void getSubItems(int itemID, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(itemID, 1, 0));
    }

    public void onBlockReplaced() {

    }

    public boolean onBlockEventReceived(int eventID) {
        return false;
    }

    public void onNeighborBlockChange(int neighborID) {

    }

    public void updateTick(Random random) {

    }

    public void updateTileEntity() {

    }

    public int isProvidingStrongPower(int side) {
        return 0;
    }

    public int isProvidingWeakPower(int side) {
        return 0;
    }

    public boolean canProvidePower() {
        return false;
    }

    public void randomDisplayTick(Random random) {

    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean shouldSideBeRendered(int side) {
        return true;
    }

    @Override
    public BlockMinechanics clone() {
        try {
            return getClass().getDeclaredConstructor(int.class).newInstance(blockID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
