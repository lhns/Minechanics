package com.dafttech.minechanics.blocks.water;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.dafttech.minechanics.data.ModConfig;
import com.dafttech.minechanics.data.ModInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NewBlockPistonBase extends BlockPistonBase {
    /** This pistons is the sticky one? */
    private final boolean isSticky;
    @SideOnly(Side.CLIENT)
    /** Only visible when piston is extended */
    private Icon innerTopIcon;
    @SideOnly(Side.CLIENT)
    /** Bottom side texture */
    private Icon bottomIcon;
    @SideOnly(Side.CLIENT)
    /** Top icon of piston depends on (either sticky or normal) */
    private Icon topIcon;

    public static Map<Float, Integer> mobilityFlag = new HashMap<Float, Integer>();

    public NewBlockPistonBase(int blockID, boolean sticky) {
        super(blockID, sticky);
        isSticky = sticky;
    }

    public static int getMobilityFlag(int id, int meta) {
        return mobilityFlag.containsKey(id + (float) meta / 100) ? mobilityFlag.get(id + (float) meta / 100) : Block.blocksList[id].getMobilityFlag();
    }

    public static void setMobilityFlag(int id, int meta, int flag) {
        mobilityFlag.put(id + (float) meta / 100, flag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Return the either 106 or 107 as the texture index depending on the isSticky flag. This will actually never get
     * called by TileEntityRendererPiston.renderPiston() because TileEntityPiston.shouldRenderHead() will always return
     * false.
     */
    public Icon getPistonExtensionTexture() {
        return topIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void func_96479_b(float par1, float par2, float par3, float par4, float par5, float par6) {
        setBlockBounds(par1, par2, par3, par4, par5, par6);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side, int meta) {
        int k = getOrientation(meta);
        return k > 5 ? topIcon : side == k ? !isExtended(meta) && minX <= 0.0D && minY <= 0.0D && minZ <= 0.0D && maxX >= 1.0D && maxY >= 1.0D
                && maxZ >= 1.0D ? topIcon : innerTopIcon : side == Facing.oppositeSide[k] ? bottomIcon : blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister) {
        blockIcon = par1IconRegister.registerIcon("piston_side");
        topIcon = par1IconRegister.registerIcon(isSticky ? "piston_top_sticky" : "piston_top_normal");
        innerTopIcon = par1IconRegister.registerIcon("piston_inner");
        bottomIcon = par1IconRegister.registerIcon("piston_bottom");
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType() {
        return 16;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether
     * or not to render the shared face of two adjacent blocks and also whether
     * the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        return false;
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        int l = determineOrientation(par1World, par2, par3, par4, par5EntityLivingBase);
        par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);

        if (!par1World.isRemote) {
            updatePistonState(par1World, par2, par3, par4);
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        if (!par1World.isRemote) {
            updatePistonState(par1World, par2, par3, par4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        if (!par1World.isRemote && par1World.getBlockTileEntity(par2, par3, par4) == null) {
            updatePistonState(par1World, par2, par3, par4);
        }
    }

    /**
     * handles attempts to extend or retract the piston.
     */
    private void updatePistonState(World par1World, int par2, int par3, int par4) {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        int i1 = getOrientation(l);

        if (i1 != 7) {
            boolean flag = isIndirectlyPowered(par1World, par2, par3, par4, i1);

            if (flag && !isExtended(l)) {
                if (canExtend(par1World, par2, par3, par4, i1)) {
                    par1World.addBlockEvent(par2, par3, par4, blockID, 0, i1);
                }
            } else if (!flag && isExtended(l)) {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i1, 2);
                par1World.addBlockEvent(par2, par3, par4, blockID, 1, i1);
            }
        }
    }

    /**
     * checks the block to that side to see if it is indirectly powered.
     */
    private boolean isIndirectlyPowered(World par1World, int par2, int par3, int par4, int par5) {
        return par5 != 0 && par1World.getIndirectPowerOutput(par2, par3 - 1, par4, 0) ? true : par5 != 1
                && par1World.getIndirectPowerOutput(par2, par3 + 1, par4, 1) ? true : par5 != 2
                && par1World.getIndirectPowerOutput(par2, par3, par4 - 1, 2) ? true : par5 != 3
                && par1World.getIndirectPowerOutput(par2, par3, par4 + 1, 3) ? true : par5 != 5
                && par1World.getIndirectPowerOutput(par2 + 1, par3, par4, 5) ? true : par5 != 4
                && par1World.getIndirectPowerOutput(par2 - 1, par3, par4, 4) ? true : par1World.getIndirectPowerOutput(par2, par3, par4, 0) ? true
                : par1World.getIndirectPowerOutput(par2, par3 + 2, par4, 1) ? true
                        : par1World.getIndirectPowerOutput(par2, par3 + 1, par4 - 1, 2) ? true : par1World.getIndirectPowerOutput(par2, par3 + 1,
                                par4 + 1, 3) ? true : par1World.getIndirectPowerOutput(par2 - 1, par3 + 1, par4, 4) ? true : par1World
                                .getIndirectPowerOutput(par2 + 1, par3 + 1, par4, 5);
    }

    /**
     * Called when the block receives a BlockEvent - see World.addBlockEvent. By
     * default, passes it on to the tile entity at this location. Args: world,
     * x, y, z, blockID, EventID, event parameter
     */
    @Override
    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
        if (!par1World.isRemote) {
            boolean flag = isIndirectlyPowered(par1World, par2, par3, par4, par6);

            if (flag && par5 == 1) {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, par6 | 8, 2);
                return false;
            }

            if (!flag && par5 == 0) {
                return false;
            }
        }

        if (par5 == 0) {
            if (!tryExtend(par1World, par2, par3, par4, par6)) {
                return false;
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, par6 | 8, 2);
            par1World.playSoundEffect(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D, "tile.piston.out", 0.5F, par1World.rand.nextFloat() * 0.25F + 0.6F);
        } else if (par5 == 1) {
            TileEntity tileentity = par1World.getBlockTileEntity(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4
                    + Facing.offsetsZForSide[par6]);

            if (tileentity instanceof TileEntityPiston) {
                ((TileEntityPiston) tileentity).clearPistonTileEntity();
            }

            par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, par6, 3);
            par1World.setBlockTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(blockID, par6, par6, false, true));

            if (isSticky) {
                int j1 = par2 + Facing.offsetsXForSide[par6] * 2;
                int k1 = par3 + Facing.offsetsYForSide[par6] * 2;
                int l1 = par4 + Facing.offsetsZForSide[par6] * 2;
                int i2 = par1World.getBlockId(j1, k1, l1);
                int j2 = par1World.getBlockMetadata(j1, k1, l1);
                boolean flag1 = false;

                if (i2 == Block.pistonMoving.blockID) {
                    TileEntity tileentity1 = par1World.getBlockTileEntity(j1, k1, l1);

                    if (tileentity1 instanceof TileEntityPiston) {
                        TileEntityPiston tileentitypiston = (TileEntityPiston) tileentity1;

                        if (tileentitypiston.getPistonOrientation() == par6 && tileentitypiston.isExtending()) {
                            tileentitypiston.clearPistonTileEntity();
                            i2 = tileentitypiston.getStoredBlockID();
                            j2 = tileentitypiston.getBlockMetadata();
                            flag1 = true;
                        }
                    }
                }

                if (!flag1
                        && i2 > 0
                        && canPushBlock(i2, par1World, j1, k1, l1, false, false)
                        && (getMobilityFlag(i2, j2) == ModInfo.MOBILITY_PUSHABLE || getMobilityFlag(i2, j2) == ModInfo.MOBILITY_PRESSURE
                                || i2 == Block.pistonBase.blockID || i2 == Block.pistonStickyBase.blockID)) {
                    par2 += Facing.offsetsXForSide[par6];
                    par3 += Facing.offsetsYForSide[par6];
                    par4 += Facing.offsetsZForSide[par6];
                    par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, j2, 3);
                    par1World.setBlockTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(i2, j2, par6, false, false));
                    par1World.setBlockToAir(j1, k1, l1);
                } else if (!flag1) {
                    par1World.setBlockToAir(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4
                            + Facing.offsetsZForSide[par6]);
                }
            } else {
                par1World
                        .setBlockToAir(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6]);
            }

            par1World.playSoundEffect(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D, "tile.piston.in", 0.5F, par1World.rand.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y,
     * z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if (isExtended(l)) {
            switch (getOrientation(l)) {
            case 0:
                setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
            case 1:
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
                break;
            case 2:
                setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
                break;
            case 3:
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
                break;
            case 4:
                setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
            case 5:
                setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            }
        } else {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add
     * boxes to the list if they intersect the mask.) Parameters: World, X, Y,
     * Z, mask, list, colliding entity
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List,
            Entity par7Entity) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this
     * box can change after the pool has been cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * If this block doesn't render as an ordinary block it will return False
     * (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * returns an int which describes the direction the piston faces
     */
    public static int getOrientation(int par0) {
        return par0 & 7;
    }

    /**
     * Determine if the metadata is related to something powered.
     */
    public static boolean isExtended(int par0) {
        return (par0 & 8) != 0;
    }

    /**
     * gets the way this piston should face for that entity that placed it.
     */
    public static int determineOrientation(World par0World, int par1, int par2, int par3, EntityLiving par4EntityLiving) {
        if (MathHelper.abs((float) par4EntityLiving.posX - par1) < 2.0F && MathHelper.abs((float) par4EntityLiving.posZ - par3) < 2.0F) {
            double d0 = par4EntityLiving.posY + 1.82D - par4EntityLiving.yOffset;

            if (d0 - par2 > 2.0D) {
                return 1;
            }

            if (par2 - d0 > 0.0D) {
                return 0;
            }
        }

        int l = MathHelper.floor_double(par4EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        return l == 0 ? 2 : l == 1 ? 5 : l == 2 ? 3 : l == 3 ? 4 : 0;
    }

    /**
     * returns true if the piston can push the specified block
     */
    private static boolean canPushBlock(int par0, World par1World, int par2, int par3, int par4, boolean par5, boolean pressure) {
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        if (par0 == Block.obsidian.blockID) {
            return false;
        } else {
            if (pressure && getMobilityFlag(par0, meta) == ModInfo.MOBILITY_PUSHABLE) return false;

            if (par0 != Block.pistonBase.blockID && par0 != Block.pistonStickyBase.blockID) {
                if (Block.blocksList[par0].getBlockHardness(par1World, par2, par3, par4) == -1.0F) {
                    return false;
                }

                if (getMobilityFlag(par0, meta) == ModInfo.MOBILITY_LOCKED) return false;

                if (getMobilityFlag(par0, meta) == ModInfo.MOBILITY_BREAKABLE) {
                    if (!par5) return false;
                    return true;
                }
            } else if (isExtended(par1World.getBlockMetadata(par2, par3, par4))) {
                return false;
            }

            return !par1World.blockHasTileEntity(par2, par3, par4);
        }
    }

    /**
     * checks to see if this piston could push the blocks in front of it.
     */
    private static boolean canExtend(World world, int x, int y, int z, int dir) {
        int xOff = x + Facing.offsetsXForSide[dir];
        int yOff = y + Facing.offsetsYForSide[dir];
        int zOff = z + Facing.offsetsZForSide[dir];
        int offset = 0;
        boolean pressure = false;

        while (true) {
            if (offset < ModConfig.pistonPushLength + 1) {
                if (yOff <= 0 || yOff >= world.getHeight() - 1) {
                    return false;
                }

                int id = world.getBlockId(xOff, yOff, zOff);
                int meta = world.getBlockMetadata(xOff, yOff, zOff);

                if (id != 0) {
                    if (!canPushBlock(id, world, xOff, yOff, zOff, true, pressure)) {
                        return false;
                    }

                    if (getMobilityFlag(id, meta) == ModInfo.MOBILITY_PRESSURE) pressure = true;

                    if (getMobilityFlag(id, meta) != ModInfo.MOBILITY_BREAKABLE) {
                        if (offset == ModConfig.pistonPushLength) {
                            return false;
                        }

                        xOff += Facing.offsetsXForSide[dir];
                        yOff += Facing.offsetsYForSide[dir];
                        zOff += Facing.offsetsZForSide[dir];
                        ++offset;
                        continue;
                    }
                }
            }

            return true;
        }
    }

    /**
     * attempts to extend the piston. returns false if impossible.
     */
    private boolean tryExtend(World world, int x, int y, int z, int dir) {
        int xOff = x + Facing.offsetsXForSide[dir];
        int yOff = y + Facing.offsetsYForSide[dir];
        int zOff = z + Facing.offsetsZForSide[dir];
        int offset = 0;
        boolean pressure = false;

        while (true) {
            int id, meta;

            if (offset < ModConfig.pistonPushLength + 1) {
                if (yOff <= 0 || yOff >= world.getHeight() - 1) {
                    return false;
                }

                id = world.getBlockId(xOff, yOff, zOff);
                meta = world.getBlockMetadata(xOff, yOff, zOff);

                if (id != 0) {
                    if (ModConfig.waterMechanics && ModConfig.pistonMechanics && offset == 0 && Liquid.isLiquidAtAll(id)) {
                        Liquid liquid = Liquid.getById(id);
                        if (liquid.isFlowing(id, meta)) {
                            liquid.setSource(world, xOff, yOff, zOff);
                            if (canExtend(world, x, y, z, dir)) {
                                world.setBlockMetadataWithNotify(xOff, yOff, zOff, meta, ModInfo.NOTIFY_MARKBLOCK);
                                boolean[] pushedLiquidBlocks = new boolean[ModConfig.pistonPushLength - 1];
                                int tmpXOff = xOff, tmpYOff = yOff, tmpZOff = zOff;
                                for (int i = 0; i < ModConfig.pistonPushLength - 1; i++) {
                                    tmpXOff += Facing.offsetsXForSide[dir];
                                    tmpYOff += Facing.offsetsYForSide[dir];
                                    tmpZOff += Facing.offsetsZForSide[dir];
                                    if (liquid.isSource(world, tmpXOff, tmpYOff, tmpZOff)) {
                                        pushedLiquidBlocks[i] = true;
                                        liquid.setFlowing(world, tmpXOff, tmpYOff, tmpZOff);
                                    }
                                }
                                if (liquid.removeSource(world, xOff, yOff, zOff, true, false)) {
                                    liquid.setSource(world, xOff, yOff, zOff);
                                    meta = world.getBlockMetadata(xOff, yOff, zOff);
                                }
                                tmpXOff = xOff;
                                tmpYOff = yOff;
                                tmpZOff = zOff;
                                for (int i = 0; i < ModConfig.pistonPushLength - 1; i++) {
                                    tmpXOff += Facing.offsetsXForSide[dir];
                                    tmpYOff += Facing.offsetsYForSide[dir];
                                    tmpZOff += Facing.offsetsZForSide[dir];
                                    if (pushedLiquidBlocks[i]) liquid.setSource(world, tmpXOff, tmpYOff, tmpZOff);
                                }
                            } else {
                                world.setBlockMetadataWithNotify(xOff, yOff, zOff, meta, ModInfo.NOTIFY_MARKBLOCK);
                            }
                        }
                    }

                    if (!canPushBlock(id, world, xOff, yOff, zOff, true, pressure)) return false;

                    if (getMobilityFlag(id, meta) == ModInfo.MOBILITY_PRESSURE) pressure = true;

                    if (getMobilityFlag(id, meta) != ModInfo.MOBILITY_BREAKABLE) {
                        if (offset == ModConfig.pistonPushLength) {
                            return false;
                        }

                        xOff += Facing.offsetsXForSide[dir];
                        yOff += Facing.offsetsYForSide[dir];
                        zOff += Facing.offsetsZForSide[dir];
                        ++offset;
                        continue;
                    }

                    // With our change to how snowballs are dropped this needs
                    // to dissallow to mimic vanilla behavior.
                    float chance = Block.blocksList[id] instanceof BlockSnow ? -1.0f : 1.0f;
                    Block.blocksList[id].dropBlockAsItemWithChance(world, xOff, yOff, zOff, world.getBlockMetadata(xOff, yOff, zOff), chance, 0);
                    world.setBlockToAir(xOff, yOff, zOff);
                }
            }

            offset = xOff;
            id = yOff;
            int j2 = zOff;
            int k2 = 0;
            int[] aint;
            int l2;
            int i3;
            int j3;

            for (aint = new int[ModConfig.pistonPushLength + 1]; xOff != x || yOff != y || zOff != z; zOff = j3) {
                l2 = xOff - Facing.offsetsXForSide[dir];
                i3 = yOff - Facing.offsetsYForSide[dir];
                j3 = zOff - Facing.offsetsZForSide[dir];
                int k3 = world.getBlockId(l2, i3, j3);
                int l3 = world.getBlockMetadata(l2, i3, j3);

                if (k3 == blockID && l2 == x && i3 == y && j3 == z) {
                    world.setBlock(xOff, yOff, zOff, Block.pistonMoving.blockID, dir | (isSticky ? 8 : 0), 4);
                    world.setBlockTileEntity(xOff, yOff, zOff,
                            BlockPistonMoving.getTileEntity(Block.pistonExtension.blockID, dir | (isSticky ? 8 : 0), dir, true, false));
                } else {
                    world.setBlock(xOff, yOff, zOff, Block.pistonMoving.blockID, l3, 4);
                    world.setBlockTileEntity(xOff, yOff, zOff, BlockPistonMoving.getTileEntity(k3, l3, dir, true, false));
                }

                aint[k2++] = k3;
                xOff = l2;
                yOff = i3;
            }

            xOff = offset;
            yOff = id;
            zOff = j2;

            for (k2 = 0; xOff != x || yOff != y || zOff != z; zOff = j3) {
                l2 = xOff - Facing.offsetsXForSide[dir];
                i3 = yOff - Facing.offsetsYForSide[dir];
                j3 = zOff - Facing.offsetsZForSide[dir];
                world.notifyBlocksOfNeighborChange(l2, i3, j3, aint[k2++]);
                xOff = l2;
                yOff = i3;
            }

            return true;
        }
    }
}
