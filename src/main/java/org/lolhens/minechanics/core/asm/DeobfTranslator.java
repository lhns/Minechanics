package org.lolhens.minechanics.core.asm;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class DeobfTranslator {
    private static final Map<String, String> deobfNames = new HashMap<String, String>();

    public static final String updateTick;
    public static final String onBlockAdded;
    public static final String getSmallestFlowDecay;
    public static final String flowIntoBlock;

    static {
        updateTick = register("net.minecraft.block.BlockDynamicLiquid", "func_149674_a", "updateTick");
        onBlockAdded = register("net.minecraft.block.BlockDynamicLiquid", "func_149726_b", "onBlockAdded");
        getSmallestFlowDecay = register("net.minecraft.block.BlockDynamicLiquid", "func_149810_a", "getSmallestFlowDecay");
        flowIntoBlock = register("net.minecraft.block.BlockDynamicLiquid", "func_149807_p", "flowIntoBlock");
    }

    private String owner, deobfOwner;

    protected DeobfTranslator(String owner, String deobfOwner) {
        this.owner = owner;
        this.deobfOwner = deobfOwner;
    }

    public String getDeobfName(String name, String desc) {
        String srgName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
        String deobfName = deobfNames.get(deobfOwner + "|" + srgName);
        if (deobfName == null) return srgName;
        return deobfName;
    }

    public String getDeobfName(MethodNode method) {
        return getDeobfName(method.name, method.desc);
    }

    private static String register(String deobfOwner, String srgName, String deobfName) {
        deobfNames.put(deobfOwner + "|" + srgName, deobfName);
        return deobfName;
    }
}
