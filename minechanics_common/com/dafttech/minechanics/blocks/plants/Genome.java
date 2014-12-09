package com.dafttech.minechanics.blocks.plants;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.dafttech.minechanics.Minechanics;
import com.dafttech.minechanics.blocks.plants.Genetics.SubGene;

public class Genome {
    Map<String, String> genes = new HashMap<String, String>();
    String name;
    TileEntityPlant tileEntity = null;

    public static Genome getGenomeAt(IBlockAccess world, int x, int y, int z) {
        if (world.getBlockTileEntity(x, y, z) == null) return null;
        return ((TileEntityPlant) world.getBlockTileEntity(x, y, z)).genome;
    }

    public static Genome getChildGenomeAt(IBlockAccess world, int x, int y, int z) {
        return ((TileEntityPlant) world.getBlockTileEntity(x, y, z)).child;
    }

    public static Genome getGenomeFromNBT(NBTTagCompound nbt, String genome) {
        Genome g = new Genome(genome);
        g.pullFromNBT(nbt);
        return g;
    }

    public boolean canGrowAt(World world, int x, int y, int z) {
        for (Gene g : Gene._GENES) {
            if (!g.canGrowAt(this, world, x, y, z)) return false;
        }
        return true;
    }

    public boolean canGrowOnce(int state, boolean isPollinated, World world, int x, int y, int z) {
        if (state >= 4 && !isPollinated || state == 7) {
            return false;
        }
        Random rnd = new Random();
        int modifier = 0;
        for (Gene g : Gene._GENES) {
            if (!g.canGrowAt(this, world, x, y, z)) {
                return false;
            }
            modifier += g.getGrowModifier(this, state, world, x, y, z);
        }
        boolean res = rnd.nextInt(20000) < modifier;
        if (res) {
            for (Gene g : Gene._GENES) {
                g.doGrowAt(this, world, x, y, z);
            }
        }
        return res;
    }

    public Genome setName(String n) {
        name = n;
        return this;
    }

    public Genome rename(String n) {
        Genome g = new Genome(n, tileEntity);
        g.genes = new HashMap<String, String>(genes);
        return g;
    }

    public Genome(String n, TileEntityPlant tileEntity) {
        name = n;
        this.tileEntity = tileEntity;
    }

    public Genome(String n) {
        this(n, null);
    }

    public String getGeneCode(String gene) {
        if (!genes.containsKey(gene)) return "";
        return genes.get(gene);
    }

    public void writeToChat() {
        if (Minechanics.proxy.isClient()) {
            StringBuilder bld = new StringBuilder();
            for (Gene g : Gene._GENES) {
                bld.append(g.name + ": " + g.getGeneCode(SubGene.AGene, this) + "/" + g.getGeneCode(SubGene.BGene, this) + " ("
                        + g.getGeneCodeByHeredity(this) + ")\n");
            }
            Minecraft.getMinecraft().thePlayer.addChatMessage(bld.toString());
        }
    }

    public void setGeneCode(String gene, String value) {
        genes.put(gene, value);
    }

    public void pushToNBT(NBTTagCompound nbt) {
        for (Gene g : Gene._GENES) {
            g.pushToNBT(this, nbt);
        }
    }

    public void pullFromNBT(NBTTagCompound nbt) {
        for (Gene g : Gene._GENES) {
            g.pullFromNBT(this, nbt);
        }
    }

    public void fillRandom() {
        for (Gene g : Gene._GENES) {
            g.fillRandom(this);
        }
    }

    public int getTexture() {
        if (tileEntity != null) {
            for (Gene g : Gene._GENES) {
                if (g.getTexture(this, tileEntity.state) != 127) {
                    return g.getTexture(this, tileEntity.state);
                }
            }
        }
        return 127;
    }

    public boolean isBiomeColored() {
        for (Gene g : Gene._GENES) {
            if (g.isBiomeColored(this)) {
                return true;
            }
        }
        return false;
    }

    public Icon getSeedIcon() {
        return null;// "seed_0";
    }

    public Icon getPollenIcon() {
        return null;// "pollen_0";
    }

    public void pollinate(Genome with) {
        for (Gene g : Gene._GENES) {
            g.crossbreedGeneIntoGenome(this, with);
        }
    }

    public int getRenderHeight() {
        if (tileEntity != null) {
            for (Gene g : Gene._GENES) {
                if (g.getRenderHeight(this, tileEntity.state) != -1) {
                    return g.getRenderHeight(this, tileEntity.state);
                }
            }
        }
        return 1;
    }
}
