package com.dafttech.minechanics.blocks.plants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.dafttech.minechanics.blocks.plants.Genetics.Heredity;
import com.dafttech.minechanics.blocks.plants.Genetics.SubGene;
import com.dafttech.minechanics.blocks.plants.genes.GeneHumidity;
import com.dafttech.minechanics.blocks.plants.genes.GeneNutrition;
import com.dafttech.minechanics.blocks.plants.genes.GeneSpecies;

public abstract class Gene {
    public static List<Gene> _GENES = new ArrayList<Gene>();
    public static Random rnd = new Random();
    public static final Gene gene_Species = new GeneSpecies();
    public static final Gene gene_Nutrition = new GeneNutrition();
    public static final Gene gene_Humidity = new GeneHumidity();
    public Map<Integer, Integer> scoreByBlockID = new HashMap<Integer, Integer>();

    public static void initAll() {
        Gene.addGene(gene_Species, "species", "Species");
        Gene.addGene(gene_Nutrition, "nutrition", "Nutrition");
        Gene.addGene(gene_Humidity, "humidity", "Humidity");
    }

    public static void addGene(Gene g, String id, String n) {
        g.identifier = id;
        g.name = n;
        _GENES.add(g);
    }

    protected String identifier = "";
    protected String name = "";
    protected Map<String, Heredity> mutations = new HashMap<String, Heredity>();

    public String getGeneCode(SubGene gene, Genome genome) {
        String value = genome.getGeneCode(identifier + (gene == SubGene.AGene ? "A" : "B"));
        if (value.equals("")) {
            System.out.println("Filled up Genome with null value.");
            setGeneCode(SubGene.AGene, genome, getNullValueForFillup());
            setGeneCode(SubGene.BGene, genome, getNullValueForFillup());
            return getGeneCode(gene, genome);
        }
        return value;
    }

    public String getNullValueForFillup() {
        return "0";
    }

    public void setGeneCode(SubGene gene, Genome genome, String value) {
        genome.setGeneCode(identifier + (gene == SubGene.AGene ? "A" : "B"), value);
    }

    public void pushToNBT(Genome genome, NBTTagCompound nbt) {
        nbt.setString(genome.name + identifier + "A", getGeneCode(SubGene.AGene, genome));
        nbt.setString(genome.name + identifier + "B", getGeneCode(SubGene.BGene, genome));
    }

    public void pullFromNBT(Genome genome, NBTTagCompound nbt) {
        if (nbt != null) {
            setGeneCode(SubGene.AGene, genome, nbt.getString(genome.name + identifier + "A"));
            setGeneCode(SubGene.BGene, genome, nbt.getString(genome.name + identifier + "B"));
        }
    }

    public void fillRandom(Genome genome) {
        String[] set = new String[mutations.keySet().size()];
        set = mutations.keySet().toArray(set);
        setGeneCode(SubGene.AGene, genome, set[rnd.nextInt(set.length)]);
        setGeneCode(SubGene.BGene, genome, set[rnd.nextInt(set.length)]);
    }

    private int scoreInBlock(World world, int x, int y, int z) {
        int block = world.getBlockId(x, y, z);
        if (scoreByBlockID.containsKey(block)) {
            // TODO if (Liquid.isSourceAtAll(world, x, y, z)) return 3 *
            // scoreByBlockID.get(block);
            return scoreByBlockID.get(block);
        }
        return 0;
    }

    public int scoreAt(World world, int x, int y, int z) {
        int have = 0;
        have += 4 * scoreInBlock(world, x, y, z);
        have += 2 * scoreInBlock(world, x + 1, y, z);
        have += 2 * scoreInBlock(world, x - 1, y, z);
        have += 2 * scoreInBlock(world, x, y, z + 1);
        have += 2 * scoreInBlock(world, x, y, z + 1);
        have += 1 * scoreInBlock(world, x + 1, y, z + 1);
        have += 1 * scoreInBlock(world, x + 1, y, z - 1);
        have += 1 * scoreInBlock(world, x - 1, y, z + 1);
        have += 1 * scoreInBlock(world, x - 1, y, z - 1);
        return have;
    }

    public int percentageFor(World world, int x, int y, int z) {
        if (Genome.getGenomeAt(world, x, y, z) != null) {
            return (int) (100 * ((double) scoreAt(world, x, y - 1, z) / Integer.parseInt(getGeneCodeByHeredity(Genome.getGenomeAt(world, x, y, z)))));
        }
        return -1;
    }

    public String getGeneCodeByHeredity(Genome g) {
        String geneA = getGeneCode(SubGene.AGene, g);
        String geneB = getGeneCode(SubGene.BGene, g);
        String gene = geneB;
        if (!(mutations.get(geneA) == Heredity.Intermediate || mutations.get(geneB) == Heredity.Intermediate)) {
            if (mutations.get(geneA) == Heredity.Dominant) {
                gene = geneA;
            }
        } else {
            gene = String.valueOf((Integer.parseInt(geneA) + Integer.parseInt(geneB)) / 2);
        }
        if (gene.equals("") || gene == null) {
            throw new IllegalStateException("Tried to get Gene with value null");
        }
        return gene;
    }

    public void crossbreedGeneIntoGenome(Genome on, Genome b) {
        boolean retA = rnd.nextDouble() > 0.5;
        boolean retB = rnd.nextDouble() > 0.5;
        SubGene sgA = retA ? SubGene.AGene : SubGene.BGene;
        SubGene sgB = retB ? SubGene.AGene : SubGene.BGene;
        setGeneCode(SubGene.AGene, on, getGeneCode(sgA, on));
        setGeneCode(SubGene.AGene, on, getGeneCode(sgB, b));
        // TODO: Mutations
    }

    public int getTexture(Genome g, int s) {
        return 127;
    }

    public boolean isBiomeColored(Genome g) {
        return false;
    }

    public int getGrowModifier(Genome genome, int state, World world, int x, int y, int z) {
        return 0;
    }

    public ArrayList<ItemStack> getSpecialDrops(Genome genome, int state, World world, int x, int y, int z) {
        return new ArrayList<ItemStack>();
    }

    public boolean canGrowAt(Genome g, World world, int x, int y, int z) {
        return true;
    }

    public void doGrowAt(Genome g, World world, int x, int y, int z) {
        return;
    }

    public int getRenderHeight(Genome g, int state) {
        return -1;
    }
}
