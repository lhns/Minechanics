package com.dafttech.minechanics.util;

import net.minecraft.world.World;

public class Effect {
    public static final Effect instance = new Effect();

    public void fizz(World world, int x, int y, int z) {
        world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        ParticleManager.spawnParticle(world, x + 0.5, y + 0.5, z + 0.5, 0.0D, 0.02D, 0.0D, "cloud", 8);
    }

    public void evap(World world, int x, int y, int z) {
        world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "liquid.lava", 0.5F, 3F);
        ParticleManager.spawnParticle(world, x + 0.5, y + 0.8, z + 0.5, 0.0D, 0.02D, 0.0D, "cloud", 8);
        ParticleManager.spawnParticle(world, x + 0.5, y + 0, z + 0.5, 0.0D, 0.02D, 0.0D, "bubble", 20);
        ParticleManager.setCustomBounds(1, 0, 1);
        ParticleManager.spawnParticle(world, x + 0.5, y + 1, z + 0.5, 0.0D, 0.02D, 0.0D, "splash", 6);
        ParticleManager.resetCustomBounds();
    }

    public void sicker(World world, int x, int y, int z) {
        world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "liquid.lava", 0.1F, 4F);
        ParticleManager.spawnParticle(world, x + 0.5, y + 0, z + 0.5, 0.0D, 0.02D, 0.0D, "bubble", 20);
    }

    public void bubble(World world, int x, int y, int z) {
        ParticleManager.setCustomBounds(2, 2, 2);
        ParticleManager.spawnParticle(world, x + 0.5, y + 0.5, z + 0.5, 0.0D, 0.02D, 0.0D, "bubble", 20);
        ParticleManager.resetCustomBounds();
    }

    public void suck(World world, int x, int y, int z) {
        world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.drink", 0.1F, 1.6F);
        bubble(world, x, y, z);
    }
}
