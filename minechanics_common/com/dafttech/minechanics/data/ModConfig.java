package com.dafttech.minechanics.data;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.blocks.data.BlockConfig;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ModConfig {
    public static ModConfig instance = new ModConfig();

    public static Class<?> blockConfig = BlockConfig.class;

    public static boolean enderSpongeCrafting = true;
    public static boolean infiniteSource = true;
    public static int infiniteSourceId = Block.blockLapis.blockID;
    public static boolean vanillaWaterSpawner = true;
    public static int vanillaWaterSpawnerId = Block.cobblestoneMossy.blockID;
    public static boolean waterMechanics = true;
    public static boolean pistonMechanics = true;
    public static boolean environment = true;
    public static int evaporationDelay = 12;
    public static int rainRefillTries = 2;
    public static int maxTickBorder = 600;
    public static boolean liquid_water = true;
    public static boolean liquid_lava = true;
    public static boolean tickLagPrevent = true;
    public static boolean tickLogInfo = false;
    public static int item_seeds = 4001;
    public static int item_pollen = 4002;
    public static boolean underwaterRenderer = true;
    public static int item_toolbox = 4003;
    public static boolean pistonPushEntities = true;
    public static int pistonPushLength = 12;
    public static boolean fancyWaterUpdates = false;
    public static boolean fluidFastMode = true;

    // Auto-Value-Detection
    @EventListener("INITPRE")
    public void loadConfig(Event event) {
        Configuration config = new Configuration(event.getInput(0, FMLPreInitializationEvent.class).getSuggestedConfigurationFile());
        config.load();
        loadValues(config, ModConfig.class);
        config.save();
    }

    public void loadValues(Configuration config, Class<?> configClass) {
        Field[] fields = configClass.getFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            String name = field.getName();
            String cath = "options";
            if (type != ModConfig.class) {
                if (!name.startsWith("block_") && !name.startsWith("item_") && name.contains("_")) {
                    cath = name.substring(0, name.indexOf("_"));
                    name = name.substring(name.indexOf("_") + 1);
                }
                try {
                    if (type == boolean.class) {
                        field.setBoolean(null, config.get(cath, name, field.getBoolean(null)).getBoolean(field.getBoolean(null)));
                    } else if (type == int.class) {
                        if (name.startsWith("block_")) {
                            field.setInt(null, config.getBlock(name.substring(6), field.getInt(null)).getInt());
                        } else if (name.startsWith("item_")) {
                            field.setInt(null, config.getItem(name.substring(5), field.getInt(null)).getInt() - 256);
                        } else {
                            field.setInt(null, config.get(cath, name, field.getInt(null)).getInt());
                        }
                    } else if (type == double.class) {
                        field.setDouble(null, config.get(cath, name, field.getDouble(null)).getDouble(field.getDouble(null)));
                    } else if (type == Class.class) {
                        loadValues(config, (Class<?>) field.get(null));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
