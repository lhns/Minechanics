package com.dafttech.minechanics.blocks.plants.genes;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.dafttech.minechanics.blocks.plants.Gene;
import com.dafttech.minechanics.blocks.plants.Genetics.Heredity;
import com.dafttech.minechanics.blocks.plants.Genome;

public class GeneHumidity extends Gene {
    public static boolean loaded = false;

    public GeneHumidity() {
        super();
        scoreByBlockID.put(Block.waterMoving.blockID, 50);
        scoreByBlockID.put(Block.waterStill.blockID, 50);
        scoreByBlockID.put(Block.lavaMoving.blockID, -50);
        scoreByBlockID.put(Block.lavaStill.blockID, -50);
        scoreByBlockID.put(Block.sand.blockID, -10);
        scoreByBlockID.put(Block.dirt.blockID, 5);
        scoreByBlockID.put(Block.grass.blockID, 10);
        if (loaded) throw new IllegalStateException("May not load gene twice.");
        loaded = true;
        for (int i = 0; i < 1000; i++) {
            mutations.put(String.valueOf(i), Heredity.Intermediate);
        }
    }

    @Override
    public String getNullValueForFillup() {
        return "500";
    }

    @Override
    public int getGrowModifier(Genome genome, int state, World world, int x, int y, int z) {
        return percentageFor(world, x, y, z);
    }

    @Override
    public void doGrowAt(Genome g, World world, int x, int y, int z) {
        for (int ix = -1; ix <= 1; ix++) {
            for (int iz = -1; iz <= 1; iz++) {
                if (world.getBlockId(x + ix, y - 1, z + iz) == Block.waterMoving.blockID
                        || world.getBlockId(x + ix, y - 1, z + iz) == Block.waterStill.blockID) {
                    if (rnd.nextInt(1000 - Integer.parseInt(getGeneCodeByHeredity(g))) < 200) {
                        /*
                         * TODO if (Minechanics.liquid_water.removeSource(world,
                         * x + ix, y - 1, z + iz, false, false)) {
                         * Effect.sicker(world, x + ix, y - 1, z + iz); }
                         */
                    }
                }
            }
        }
    }
}
