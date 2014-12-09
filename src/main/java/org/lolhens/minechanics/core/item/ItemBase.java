package org.lolhens.minechanics.core.item;

import static org.lolhens.minechanics.core.reference.Reference.MOD_ID;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemBase extends Item {
    public ItemBase(String name, String texture) {
        setUnlocalizedName(name);
        GameRegistry.registerItem(this, name);
        setTextureName((texture.contains(":") ? "" : MOD_ID) + texture);
    }
}
