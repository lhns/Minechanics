package org.lolhens.minechanics.core.client;

import static org.lolhens.minechanics.core.reference.Reference.MOD_ID;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class ResourceType {
    public static final ResourceType GUI = new ResourceType("textures/gui", "png") {
        {
            addResourceLocation("guielements");
        }
    };

    private String path, ext;
    private Map<String, ResourceLocation> loadedResources = new HashMap<>();

    public ResourceType(String path, String ext) {
        path = path.replace("\\", "/");
        if (!path.endsWith("/")) path = path + "/";
        this.path = path;

        if (!ext.startsWith(".")) ext = "." + ext;
        this.ext = ext;
    }

    public void addResourceLocation(String name) {
        addResourceLocation(name, MOD_ID);
    }

    public void addResourceLocation(String name, String modId) {
        addResourceLocation(name, modId, path + name + ext);
    }

    public void addResourceLocation(String name, String modId, String customPath) {
        loadedResources.put(name, new ResourceLocation(modId, customPath));
    }

    public ResourceLocation getResourceLocation(String name) {
        return loadedResources.get(name);
    }

    public String getPath() {
        return path;
    }

    public String getFileExtension() {
        return ext;
    }
}