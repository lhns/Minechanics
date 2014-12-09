package com.dafttech.minechanics.blocks.machines;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import com.dafttech.minechanics.blocks.BlockMinechanics;
import com.dafttech.minechanics.client.renderer.model.Box;
import com.dafttech.minechanics.client.renderer.model.Model;
import com.dafttech.minechanics.client.renderer.model.Texture;
import com.dafttech.minechanics.util.Saved;
import com.dafttech.minechanics.util.Vector3f;

public class Conveyor extends BlockMinechanics {
    @Saved(false)
    private float anim = 0;
    @Saved
    private int dir = 0;
    @Saved
    private float speed = 1;

    public Conveyor(int blockID) {
        super(blockID);
    }

    @Override
    public void created() {
    }

    @Override
    public String getUnlocalizedName() {
        return "Conveyor";
    }

    @Override
    public String[] getName() {
        return new String[] { "Conveyor" };
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.tabRedstone;
    }

    @Override
    public float getHardness() {
        return 1;
    }

    @Override
    public void setBlockBoundsBasedOnState() {
        final float min = 0.005f, max = 0.995f;
        block.setBlockBounds(dir == 0 || dir == 2 ? min : 0, 0.005f, dir == 0 || dir == 2 ? 0 : min, dir == 0 || dir == 2 ? max : 1, 0.995f, dir == 0
                || dir == 2 ? 1 : max);
    }

    @Override
    public void onBlockAdded(EntityLivingBase entityLiving, ItemStack itemStack, int side, float hitX, float hitY, float hitZ) {
        if (entityLiving != null) {
            // ((Conveyor) getTileEntity(iba, x, y, z).getBlockMcncs()).dir =
            // ((MathHelper.floor_double(entityLiving.rotationYaw * 4.0F /
            // 360.0F + 0.5D) & 3) + 1) % 4;
            // updateTileEntity();
        } else if (side > -1) {
            dir = side;
            updateTileEntity();
        }
    }

    @Override
    public void onEntityCollidedWithBlock(Entity entity) {
        double motion = dir == 0 || dir == 2 ? entity.motionX : entity.motionZ;
        double newMotionX = dir == 0 || dir == 2 ? speed * (dir > 1 ? -1 : 1) : 0;
        double newMotionZ = speed * (dir > 1 ? -1 : 1) - newMotionX;
        if (entity.isCollidedVertically && motion < speed / 10) entity.addVelocity(newMotionX / 20, 0, newMotionZ / 20);
        if ((!entity.isCollidedVertically || entity.isCollidedHorizontally) && entity.motionY < 0.1f)
            entity.addVelocity(newMotionX / 100, 0.1f, newMotionZ / 100);
    }

    @Override
    public Model getModel(RenderMode mode, float delta) {
        anim += delta / 100 * speed;
        Model model = new Model().setTexture(new Texture("machineiron", block));
        Model base = new Model();
        model.add(base);
        base.add(new Box(0.1f, 0.1f, 0.1f, 0.8f, 0.8f, 0.8f));
        base.add(new Box(0, 0, 0, 0.1f, 1, 1));
        base.add(new Box(0.9f, 0, 0, 0.1f, 1, 1));
        Model chain = new Model();
        model.add(chain);
        Model[] chainLayer = new Model[4];
        for (int i = 0; i < 4; i++) {
            chainLayer[i] = new Model().rotate(new Vector3f((float) Math.toRadians(i * 90), 0, 0));
            if (mode == RenderMode.WORLD) {
                int id = 0;
                if (i == 0) {
                    id = iba.getBlockId(x, y + 1, z);
                } else if (i == 1) {
                    id = iba.getBlockId(x + (dir == 0 ? 1 : dir == 2 ? -1 : 0), y, z + (dir == 1 ? 1 : dir == 3 ? -1 : 0));
                } else if (i == 2) {
                    id = iba.getBlockId(x, y - 1, z);
                } else if (i == 3) {
                    id = iba.getBlockId(x + (dir == 0 ? -1 : dir == 2 ? 1 : 0), y, z + (dir == 1 ? -1 : dir == 3 ? 1 : 0));
                }
                if (id == block.blockID) chainLayer[i].setVisible(false);
            }
        }
        chain.add(chainLayer);
        float offset, rotation;
        for (int i = 0; i < 16; i++) {
            offset = anim % 1 + i * 0.25f;
            rotation = 0;
            if (offset % 1 < 0.1f && chainLayer[(int) (offset + 3) % 4].isVisible()) {
                rotation = -45 + offset % 1 * 450;
            } else if (offset % 1 > 0.9f && chainLayer[(int) (offset + 1) % 4].isVisible()) {
                rotation = (offset % 1 - 0.9f) * 450;
            } else {
                rotation = 0;
            }
            Model chainTooth = new Box(.1f, .9f, offset % 1 - .1f, .8f, .1f, .2f).setOrigin(new Vector3f(0.5f, 1, 0.5f));
            chainTooth.rotate(new Vector3f((float) Math.toRadians(rotation), 0, 0));
            chainLayer[(int) offset % 4].add(chainTooth);
        }
        return model.rotate(new Vector3f(0, (float) Math.toRadians((3 - (dir + 2)) * 90 % 360), 0));
    }
}
