package com.dafttech.minechanics.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import com.dafttech.minechanics.blocks.BlockMinechanics.RenderMode;
import com.dafttech.minechanics.client.Textures;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.util.BlockPos.BlockPos;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockUniversal extends Block implements ISimpleBlockRenderingHandler, IItemRenderer {
    private static Map<Block, Integer> renderIds = new HashMap<Block, Integer>();
    private static boolean isOpaque;

    protected BlockMinechanics blockMcncs;

    public BlockUniversal(BlockMinechanics block) {
        super(setOpaqueAndGetBlockID(block), block.getMaterial());
        blockMcncs = block;
        // blockMcncs.block = this;
        MinecraftForgeClient.registerItemRenderer(blockID, this);
        setHardness(blockMcncs.getHardness());
        CreativeTabs creativeTab = blockMcncs.getCreativeTab();
        if (creativeTab != null) setCreativeTab(creativeTab);
        GameRegistry.registerBlock(this, ItemBlockUniversal.class, blockMcncs.getUnlocalizedName(), ModInfo.MOD_ID);
        renderIds.put(this, RenderingRegistry.getNextAvailableRenderId());
        RenderingRegistry.registerBlockHandler(this);
        if (hasTileEntity(0)) {
            TileEntity tileentity = createTileEntity(null, 0);
            GameRegistry.registerTileEntity(tileentity.getClass(), blockMcncs.getUnlocalizedName() + "-" + ModInfo.MOD_ID + "Tile");
            ClientRegistry.bindTileEntitySpecialRenderer(tileentity.getClass(), new TileEntityRendererUniversal());
        }
        blockParticleGravity = 1;
        blockMcncs.created();
    }

    @Override
    public Icon getIcon(int side, int metadata) {
        return Textures.instance.getBlockTexture("invisible");
    }

    private static int setOpaqueAndGetBlockID(BlockMinechanics block) {
        isOpaque = block.isOpaqueCube();
        return block.getBlockID();
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        blockMcncs.getInstance(new BlockPos(world, x, y, z)).onBlockAdded(null, null, -1, 0, 0, 0);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
        blockMcncs.getInstance(new BlockPos(world, x, y, z)).onBlockAdded(entityLiving, itemStack, -1, 0, 0, 0);
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
     * side, hitX, hitY, hitZ, block metadata
     */
    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        blockMcncs.getInstance(new BlockPos(world, x, y, z)).onBlockAdded(null, null, -1, 0, 0, 0);
        return metadata;
    }

    /**
     * Called on server worlds only when the block has been replaced by a
     * different block ID, or the same block with a different metadata value,
     * but before the new metadata value is set. Args: World, x, y, z, old block
     * ID, old metadata
     */
    @Override
    public void breakBlock(World world, int x, int y, int z, int blockID, int metadata) {
        super.breakBlock(world, x, y, z, blockID, metadata);
        if (hasTileEntity(metadata)) world.removeBlockTileEntity(x, y, z);
        blockMcncs.getNewInstance(this, metadata, world, x, y, z).onBlockReplaced();
    }

    /**
     * Called when the block receives a BlockEvent - see World.addBlockEvent. By
     * default, passes it on to the tile entity at this location. Args: world,
     * x, y, z, blockID, EventID, event parameter
     */
    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int blockID, int eventID) {
        super.onBlockEventReceived(world, x, y, z, blockID, eventID);
        boolean ret = false;
        if (hasTileEntity(world.getBlockMetadata(x, y, z))) {
            TileEntity tileentity = world.getBlockTileEntity(x, y, z);
            ret = tileentity != null ? tileentity.receiveClientEvent(blockID, eventID) : false;
        }
        if (!ret) ret = blockMcncs.getInstance(new BlockPos(world, x, y, z)).onBlockEventReceived(eventID);
        return ret;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
        blockMcncs.getInstance(new BlockPos(world, x, y, z)).onNeighborBlockChange(neighborID);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iba, int x, int y, int z) {
        blockMcncs.getInstance(new BlockPos(world, x, y, z)).setBlockBoundsBasedOnState();
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        blockMcncs.getInstance(new BlockPos(world, x, y, z)).onEntityCollidedWithBlock(entity);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return blockMcncs.getInstance(new BlockPos(world, x, y, z)).onBlockActivated(player, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return blockMcncs.getInstance(null).hasTileEntity();
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        if (hasTileEntity(metadata)) {
            return new TileEntityUniversal();
        }
        return null;
    }

    @Override
    public int damageDropped(int metadata) {
        return blockMcncs.getInstance(null).hasSubtypes() ? metadata : 0;
    }

    @Override
    public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        Random rnd = new Random();
        for (int i = 0; i < 30; i++) {
            Minecraft.getMinecraft().effectRenderer
                    .addEffect(new EntityDiggingUniversalFX(world, x, y, z, meta, rnd.nextFloat() * 0.2f + 0.1f, this));
        }
        return super.addBlockDestroyEffects(world, x, y, z, meta, effectRenderer);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        // unused, but needed
        blockMcncs.getInstance(null).render(null, RenderMode.INVENTORY, renderer, 0);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        RenderBlocks renderer = (RenderBlocks) data[0];
        RenderMode mode;
        World world = null;
        int x = 0, y = 0, z = 0;
        if (type == ItemRenderType.ENTITY) {
            EntityItem entity = (EntityItem) data[1];
            world = entity.worldObj;
            x = MathHelper.floor_double(entity.posX);
            y = MathHelper.floor_double(entity.posY);
            z = MathHelper.floor_double(entity.posZ);
            mode = RenderMode.ENTITY;
        } else if (type == ItemRenderType.EQUIPPED) {
            mode = RenderMode.EQUIPPED;
        } else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            mode = RenderMode.EQUIPPED_FIRST_PERSON;
        } else if (type == ItemRenderType.FIRST_PERSON_MAP) {
            mode = RenderMode.FIRST_PERSON_MAP;
        } else {
            mode = RenderMode.INVENTORY;
        }
        blockMcncs.getInstance(new BlockPos(world, x, y, z)).render(mode, renderer, 0);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess iba, int x, int y, int z, Block block, int modelID, RenderBlocks renderer) {
        if (!hasTileEntity(0)) blockMcncs.getInstance(new BlockPos(world, x, y, z)).render(RenderMode.WORLD, renderer, 0);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderIds.get(this);
    }

    @Override
    public int getRenderType() {
        return getRenderId();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        blockMcncs.getInstance(new BlockPos(world, x, y, z)).updateTick(random);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess iba, int x, int y, int z, int side) {
        return blockMcncs.getInstance(new BlockPos(world, x, y, z)).isProvidingStrongPower(side);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess iba, int x, int y, int z, int side) {
        return blockMcncs.getInstance(new BlockPos(world, x, y, z)).isProvidingWeakPower(side);
    }

    @Override
    public boolean canProvidePower() {
        return blockMcncs.getInstance(new BlockPos(world, x, y, z)).canProvidePower();
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        blockMcncs.getInstance(new BlockPos(world, x, y, z)).randomDisplayTick(random);
    }

    @Override
    public boolean isOpaqueCube() {
        if (blockMcncs == null) return isOpaque;
        return blockMcncs.getInstance(new BlockPos(world, x, y, z)).isOpaqueCube();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess iba, int x, int y, int z, int side) {
        return blockMcncs.getInstance(new BlockPos(world, x, y, z)).shouldSideBeRendered(side);
    }
}