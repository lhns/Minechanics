package com.dafttech.minechanics.blocks.plants;

public class Genetics {
    public static enum SubGene {
        AGene, BGene
    }

    public static enum Influence {
        Color, HumidityTolerance, TemperatureTolerance, NutritionTolerance, GrowSpeed
    }

    public static enum Heredity {
        Dominant, Recessive, Intermediate, NotHeraditary
    }
}
