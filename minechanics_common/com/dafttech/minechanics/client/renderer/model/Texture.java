package com.dafttech.minechanics.client.renderer.model;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

import com.dafttech.minechanics.client.Textures;
import com.dafttech.minechanics.data.ModInfo;

public class Texture {
    private Icon icon;
    private int spriteNum;
    @SuppressWarnings("unused")
    private boolean stretch = true;

    public Texture(Icon icon, Block block) {
        this.icon = icon;
        spriteNum = ModInfo.SPRITENUM_TERRAIN;
    }

    public Texture(Icon icon, Item item) {
        this.icon = icon;
        spriteNum = ModInfo.SPRITENUM_ITEMS;
    }

    public Texture(Icon icon, int spriteNum) {
        this.icon = icon;
        this.spriteNum = spriteNum;
    }

    public Texture(String icon, Block block) {
        this(Textures.instance.getBlockTexture(icon), block);
    }

    public Texture(String icon, Item item) {
        this(Textures.instance.getBlockTexture(icon), item);
    }

    public Texture(String icon, int spriteNum) {
        this(Textures.instance.getBlockTexture(icon), spriteNum);
    }

    public Icon getIcon() {
        return icon;
    }

    public int getSpriteNum() {
        return spriteNum;
    }

    public Texture setStretched(boolean stretch) {
        this.stretch = stretch;
        return this;
    }

    public float getMinU() {
        return icon.getMinU();
    }

    public float getMinV() {
        return icon.getMinV();
    }

    public float getMaxU() {
        return icon.getMaxU();
    }

    public float getMaxV() {
        return icon.getMaxV();
    }

    @Deprecated
    public static int extractSpriteNum(Icon icon) {
        TextureAtlasSprite stitched = null;
        if (icon instanceof TextureAtlasSprite) {
            stitched = (TextureAtlasSprite) icon;
        } else if (icon instanceof IconFlipped) {
            IconFlipped flipped = (IconFlipped) icon;
            Field[] fields = flipped.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == Icon.class) {
                    field.setAccessible(true);
                    try {
                        Icon baseIcon = (Icon) field.get(flipped);
                        if (baseIcon instanceof TextureAtlasSprite) {
                            stitched = (TextureAtlasSprite) baseIcon;
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                    }
                }
            }
        }
        DynamicTexture sheet = null;
        if (stitched != null) {
            Field[] fields = stitched.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == DynamicTexture.class) {
                    field.setAccessible(true);
                    try {
                        sheet = (DynamicTexture) field.get(stitched);

                    } catch (IllegalArgumentException | IllegalAccessException e) {
                    }
                }
            }
        }
        if (sheet != null) {
            return sheet.getGlTextureId();
        }
        return -1;
    }
}
