package org.lolhens.minechanics;

import static org.lolhens.minechanics.core.reference.Package.DAFTTECH;
import static org.lolhens.minechanics.core.reference.Package.LOLHENS;
import static org.lolhens.minechanics.core.reference.Reference.ACCESS_TRANSFORMER;
import static org.lolhens.minechanics.core.reference.Reference.MC_VERSION;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.lolhens.minechanics.core.asm.ClassTransformer;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({ LOLHENS, DAFTTECH })
@MCVersion(MC_VERSION)
public class MinechanicsCorePlugin implements IFMLLoadingPlugin {
    public static File location;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { ClassTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getAccessTransformerClass() {
        return null;// AccessTransformer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return SetupClass.class.getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    public static class AccessTransformer extends cpw.mods.fml.common.asm.transformers.AccessTransformer {
        public AccessTransformer() throws IOException {
            super(ACCESS_TRANSFORMER);
        }
    }

    public static class SetupClass implements IFMLCallHook {
        @Override
        public Void call() throws Exception {
            ClassTransformer.registerASMListeners();
            return null;
        }

        @Override
        public void injectData(Map<String, Object> data) {
            location = (File) data.get("coremodLocation");
        }
    }
}
