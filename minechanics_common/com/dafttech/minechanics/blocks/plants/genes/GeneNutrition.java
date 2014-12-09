package com.dafttech.minechanics.blocks.plants.genes;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.dafttech.minechanics.blocks.plants.Gene;
import com.dafttech.minechanics.blocks.plants.Genetics.Heredity;
import com.dafttech.minechanics.blocks.plants.Genome;

public class GeneNutrition extends Gene {
    public static boolean loaded = false;

    public GeneNutrition() {
        super();
        scoreByBlockID.put(Block.dirt.blockID, 50);
        scoreByBlockID.put(Block.tilledField.blockID, 75);
        scoreByBlockID.put(Block.netherrack.blockID, 60);
        scoreByBlockID.put(Block.grass.blockID, 40);
        scoreByBlockID.put(Block.waterMoving.blockID, 10);
        scoreByBlockID.put(Block.waterStill.blockID, 10);
        scoreByBlockID.put(Block.cobblestoneMossy.blockID, 10);
        scoreByBlockID.put(Block.gravel.blockID, 20);
        scoreByBlockID.put(Block.sand.blockID, 20);
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
}
