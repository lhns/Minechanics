package com.dafttech.minechanics.blocks.plants.genes;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.dafttech.minechanics.blocks.plants.Gene;
import com.dafttech.minechanics.blocks.plants.Genetics.Heredity;
import com.dafttech.minechanics.blocks.plants.Genome;

public class GeneSpecies extends Gene {
    public static boolean loaded = false;

    public GeneSpecies() {
        super();
        if (loaded) throw new IllegalStateException("May not load gene twice.");
        loaded = true;
        mutations.put("Grass", Heredity.Dominant);
        mutations.put("Strawberry", Heredity.Recessive);
        mutations.put("Coffee", Heredity.Recessive);
        mutations.put("Redcurrant", Heredity.Recessive);
        mutations.put("Gooseberry", Heredity.Recessive);
        mutations.put("Wheat", Heredity.Dominant);
        mutations.put("Netherwart", Heredity.Recessive);
    }

    @Override
    public String getNullValueForFillup() {
        return "Grass";
    }

    @Override
    public int getTexture(Genome g, int s) {
        String gene = getGeneCodeByHeredity(g);
        if (gene != null) {
            if (gene.equals("Grass")) {
                return 17 + s;
            }
            if (gene.equals("Strawberry")) {
                return 0 + s;
            }
            if (gene.equals("Coffee")) {
                return 34 + s;
            }
            if (gene.equals("Redcurrant")) {
                return 64 + s;
            }
            if (gene.equals("Gooseberry")) {
                return 96 + s;
            }
            if (gene.equals("Wheat")) {
                return 112 + s;
            }
            if (gene.equals("Netherwart")) {
                return 128 + s;
            }
        }
        return 127;
    }

    @Override
    public boolean isBiomeColored(Genome g) {
        String gene = getGeneCodeByHeredity(g);
        if (gene != null) {
            if (gene.equals("Grass")) {
                return true;
            }
            if (gene.equals("Strawberry")) {
                return false;
            }
            if (gene.equals("Coffee")) {
                return true;
            }
            if (gene.equals("Redcurrant")) {
                return false;
            }
            if (gene.equals("Gooseberry")) {
                return false;
            }
            if (gene.equals("Wheat")) {
                return false;
            }
            if (gene.equals("Netherwart")) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canGrowAt(Genome g, World world, int x, int y, int z) {
        String gene = getGeneCodeByHeredity(g);
        Block blockOn = Block.blocksList[world.getBlockId(x, y - 1, z)];
        if (gene != null) {
            if (gene.equals("Grass")) {
                if (blockOn == Block.grass || blockOn == Block.dirt || blockOn == Block.sand || blockOn == Block.mycelium
                        || blockOn == Block.cobblestoneMossy) {
                    return true;
                }
            }
            if (gene.equals("Strawberry")) {
                if (blockOn == Block.grass || blockOn == Block.dirt || blockOn == Block.mycelium || blockOn == Block.tilledField) {
                    return true;
                }
            }
            if (gene.equals("Coffee")) {
                if (blockOn == Block.tilledField) {
                    return true;
                }
            }
            if (gene.equals("Redcurrant")) {
                if (blockOn == Block.tilledField) {
                    return true;
                }
            }
            if (gene.equals("Gooseberry")) {
                if (blockOn == Block.tilledField) {
                    return true;
                }
            }
            if (gene.equals("Wheat")) {
                if (blockOn == Block.tilledField) {
                    return true;
                }
            }
            if (gene.equals("Netherwart")) {
                if (blockOn == Block.slowSand) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ArrayList<ItemStack> getSpecialDrops(Genome genome, int state, World world, int x, int y, int z) {
        String gene = getGeneCodeByHeredity(genome);
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        if (gene != null) {
            if (gene.equals("Grass")) {
            }
            if (gene.equals("Strawberry")) {
                // TODO: Drop Strawberrys
            }
            if (gene.equals("Coffee")) {
                // TODO: Drop Coffee beans
            }
            if (gene.equals("Redcurrant")) {
                // TODO: Drop fruit
            }
            if (gene.equals("Gooseberry")) {
                // TODO: Drop fruit
            }
            if (gene.equals("Wheat")) {
                list.add(new ItemStack(Item.wheat, rnd.nextInt(state / 5 + 1) + 1));
            }
            if (gene.equals("Netherwart")) {
                list.add(new ItemStack(Item.netherStalkSeeds, rnd.nextInt(state / 7 + 1) + 1));
            }
        }
        return list;
    }

    @Override
    public int getGrowModifier(Genome genome, int state, World world, int x, int y, int z) {
        return 250;
    }

    @Override
    public int getRenderHeight(Genome g, int s) {
        String gene = getGeneCodeByHeredity(g);
        if (gene != null) {
            if (gene.equals("Grass")) {
                if (s == 7) return 2;
            }
            if (gene.equals("Coffee")) {
                if (s == 7) return 2;
            }
            if (gene.equals("Redcurrant")) {
                if (s == 7) return 2;
            }
            if (gene.equals("Gooseberry")) {
                if (s == 7) return 2;
            }
        }
        return 1;
    }
}
