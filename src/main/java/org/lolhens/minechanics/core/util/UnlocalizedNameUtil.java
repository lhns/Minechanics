package org.lolhens.minechanics.core.util;

import java.util.Iterator;

import net.minecraft.block.Block;

public class UnlocalizedNameUtil {
    public static Block getBlockByUnlocalizedName(String unlocalizedName) {
        unlocalizedName = unlocalizedName.toLowerCase();
        Iterator<?> iterator = Block.blockRegistry.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof Block && ((Block) obj).getUnlocalizedName().toLowerCase().equals(unlocalizedName))
                return (Block) obj;
        }
        return null;
    }
}
