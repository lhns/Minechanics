package com.dafttech.minechanics.blocks;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.blocks.data.BlockConfig;
import com.dafttech.minechanics.blocks.machines.Conveyor;
import com.dafttech.minechanics.blocks.machines.Toolbox;
import com.dafttech.minechanics.blocks.plants.BlockPlant;
import com.dafttech.minechanics.blocks.water.LiquidSensor;
import com.dafttech.minechanics.blocks.water.NewBlockFlowing;
import com.dafttech.minechanics.blocks.water.NewBlockPistonBase;
import com.dafttech.minechanics.blocks.water.NewBlockStationary;
import com.dafttech.minechanics.blocks.water.Sponge;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.util.BlockManager;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;

public class Blocks {
    public static final Blocks instance = new Blocks();

    public static Block plant;
    public static BlockFluid waterMoving;
    public static Block waterStill;
    public static NewBlockPistonBase pistonBase;
    public static NewBlockPistonBase pistonStickyBase;
    public static Block sponge;
    public static Block liquidSensor;
    public static Block toolbox;
    public static Block conveyor;

    @EventListener("INIT")
    public void registerBlocks(Event event) {
        plant = BlockManager.registerBlock(new BlockPlant(BlockConfig.block_basePlant), "Plant");

        waterMoving = (BlockFluid) new NewBlockFlowing(removeBlock(Block.waterMoving), Material.water).setHardness(100.0F).setLightOpacity(3)
                .setUnlocalizedName("water");
        waterStill = new NewBlockStationary(removeBlock(Block.waterStill), Material.water).setHardness(100.0F).setLightOpacity(3)
                .setUnlocalizedName("water");

        pistonBase = (NewBlockPistonBase) new NewBlockPistonBase(removeBlock(Block.pistonBase), false).setUnlocalizedName("pistonBase");
        pistonStickyBase = (NewBlockPistonBase) new NewBlockPistonBase(removeBlock(Block.pistonStickyBase), true)
                .setUnlocalizedName("pistonStickyBase");

        sponge = new Sponge(BlockConfig.block_sponge).register();
        liquidSensor = new LiquidSensor(BlockConfig.block_liquidSensor).register();
        toolbox = new Toolbox(BlockConfig.block_toolbox).register();
        conveyor = new Conveyor(BlockConfig.block_conveyor).register();
    }

    public static int removeBlock(Block block) {
        Block.blocksList[block.blockID] = null;
        return block.blockID;
    }

    @SuppressWarnings("unchecked")
    public static int removeItemBlock(Block block) {
        Item.itemsList[block.blockID] = null;
        for (Field field : GameData.class.getDeclaredFields()) {
            if (field.getType() == Map.class && ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0] == Integer.class) {
                field.setAccessible(true);
                try {
                    ((Map<Integer, ItemData>) field.get(null)).remove(block.blockID);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
        return block.blockID - ModInfo.ITEM_OFFSET;
    }
}
