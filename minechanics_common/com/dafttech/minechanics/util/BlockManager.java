package com.dafttech.minechanics.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

import com.dafttech.minechanics.Minechanics;
import com.dafttech.minechanics.blocks.BlockMinechanics;
import com.dafttech.minechanics.blocks.BlockUniversal;
import com.dafttech.minechanics.blocks.ItemBlockUniversal;
import com.dafttech.minechanics.blocks.TileEntityRendererUniversal;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.interfaces.ITileEntitySpecialRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BlockManager {
    private static Map<Block, Integer> renderIds = new HashMap<Block, Integer>();
    private static List<String> preloadedTextures = new ArrayList<String>();
    private static boolean clientInit = false;

    public static Item registerItem(Item item, String name) {
        item.setUnlocalizedName(name);
        GameRegistry.registerItem(item, name);
        LanguageRegistry.addName(item, name);
        return item;
    }

    public static BlockUniversal registerBlock(BlockMinechanics block) {
        return (BlockUniversal) registerBlock(block.register(), block.getUnlocalizedName());
    }

    public static Block registerBlock(Block block, String name) {
        if (Minechanics.proxy.isClient() && !clientInit) initClient();
        block.setUnlocalizedName(name);
        Class<? extends ItemBlock> itemBlockClass = ItemBlock.class;
        if (block instanceof BlockUniversal) itemBlockClass = ItemBlockUniversal.class;
        GameRegistry.registerBlock(block, itemBlockClass, name, ModInfo.MOD_ID);
        LanguageRegistry.addName(block, name);
        // autoregisterTexture(block.getTextureFile());
        if (block instanceof ISimpleBlockRenderingHandler && Minechanics.proxy.isClient()) {
            renderIds.put(block, RenderingRegistry.getNextAvailableRenderId());
            RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler) block);
        }
        if (block.hasTileEntity(0)) {
            TileEntity tileentity = block.createTileEntity(null, 0);
            GameRegistry.registerTileEntity(tileentity.getClass(), name + "-" + ModInfo.MOD_ID + "Tile");
            if (block instanceof BlockUniversal) {
                ClientRegistry.bindTileEntitySpecialRenderer(tileentity.getClass(), new TileEntityRendererUniversal());
            } else if (tileentity instanceof ITileEntitySpecialRenderer) {
                ClientRegistry
                        .bindTileEntitySpecialRenderer(tileentity.getClass(), ((ITileEntitySpecialRenderer) tileentity).getTileEntityRenderer());
            }
        }
        return block;
    }

    public static int getRenderId(Block block) {
        if (renderIds.containsKey(block)) return renderIds.get(block);
        return 0;
    }

    private static void initClient() {
        clientInit = true;
        preloadedTextures.add("/terrain.png");
    }
}
