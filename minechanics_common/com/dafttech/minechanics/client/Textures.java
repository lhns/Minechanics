package com.dafttech.minechanics.client;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.Minechanics;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.event.Events;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Textures {
    public static final Textures instance = new Textures();

    private Map<String, Icon> texturesBlock = new HashMap<String, Icon>();
    private Map<String, Icon> texturesItem = new HashMap<String, Icon>();

    public TextureMap textureMapBlocks = null;
    public TextureMap textureMapItems = null;

    @SideOnly(Side.CLIENT)
    @EventListener(value = "TEXTUREREGISTER", filter = "..data.ModInfo.spritefilter_terrain")
    protected void registerTexturesBlock(Event event) {
        registerBlockTexture("invisible");
        registerBlockTexture("clean");

        registerBlockTexture("ironbars");
        registerBlockTexture("iron");
        registerBlockTexture("wood");
        registerBlockTexture("lilypad");
        registerBlockTexture("ironbars");
        registerBlockTexture("stringblock");
        registerBlockTexture("grass_6");
        registerBlockTexture("green");
        registerBlockTexture("applestick");
        registerBlockTexture("sponge");
        registerBlockTexture("machineiron");
    }

    @SideOnly(Side.CLIENT)
    @EventListener(value = "TEXTUREREGISTER", filter = "..data.ModInfo.spritefilter_items")
    public void registerTexturesItem(Event event) {
        registerItemTexture("seed", 6);
    }

    public void registerBlockTexture(String filename) {
        if (Minechanics.proxy.isClient()) texturesBlock.put(filename, textureMapBlocks.registerIcon(ModInfo.PATH_TEXTUREPREFIX + filename));
    }

    public void registerBlockTexture(String filename, int num) {
        for (int i = 0; i <= num; i++) {
            registerBlockTexture(filename + "_" + i);
        }
    }

    public void registerItemTexture(String filename) {
        if (Minechanics.proxy.isClient()) texturesItem.put(filename, textureMapItems.registerIcon(ModInfo.PATH_TEXTUREPREFIX + filename));
    }

    public void registerItemTexture(String filename, int num) {
        for (int i = 0; i <= num; i++) {
            registerItemTexture(filename + "_" + i);
        }
    }

    public void registerBlockTexture(String filename, String prefix) {
        if (Minechanics.proxy.isClient()) texturesBlock.put(filename, textureMapBlocks.registerIcon(prefix + filename));
    }

    public void registerItemTexture(String filename, String prefix) {
        if (Minechanics.proxy.isClient()) texturesItem.put(filename, textureMapItems.registerIcon(prefix + filename));
    }

    public Icon getBlockTexture(String filename) {
        if (texturesBlock.containsKey(filename)) return texturesBlock.get(filename);
        return null;
    }

    public Icon getItemTexture(String filename) {
        if (texturesItem.containsKey(filename)) return texturesItem.get(filename);
        return null;
    }

    @EventListener("TEXTURESTITCH")
    public void registerTextures(Event event) {
        TextureMap map = event.getInput(0, TextureStitchEvent.class).map;
        if (map.textureType == ModInfo.SPRITENUM_TERRAIN) {
            textureMapBlocks = map;
        } else if (map.textureType == ModInfo.SPRITENUM_ITEMS) {
            textureMapItems = map;
        }
        Events.EVENTMANAGER.callSync(Events.EVENT_TEXTUREREGISTER, map, map.textureType);
        return;
    }
}
