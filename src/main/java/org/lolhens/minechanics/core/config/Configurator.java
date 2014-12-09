package org.lolhens.minechanics.core.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.lolhens.minechanics.core.event.Events;
import org.lolhens.minechanics.core.util.UnlocalizedNameUtil;

import com.dafttech.eventmanager.EventListener;
import com.dafttech.primitive.PrimitiveUtil;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Configurator {
    private Configuration config;
    private Class<?> configClass;
    private List<ConfigField> configFields = new ArrayList<ConfigField>();
    private List<Configurator> subConfigurators = new ArrayList<Configurator>();

    public Configurator(Configuration config, Class<?> configClass) {
        this.config = config;
        this.configClass = configClass;
        loadDefaultValues();
        Events.eventManager.registerEventListener(this);
    }

    public Configurator(FMLPreInitializationEvent event, Class<?> configClass) {
        this(new Configuration(event.getSuggestedConfigurationFile()), configClass);
    }

    private void loadDefaultValues() {
        int modifiers;
        Class<?> type;
        for (Field field : configClass.getFields()) {
            modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                type = field.getType();
                if (type == Class.class) {
                    Class<?> configClass = null;
                    try {
                        configClass = (Class<?>) field.get(null);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (configClass != null) subConfigurators.add(new Configurator(config, configClass));
                } else {
                    ConfigValueType confValType = ConfigValueType.forClass(type);
                    if (confValType != null) configFields.add(new ConfigField(field, confValType));
                }
            }
        }
        for (Configurator configurator : subConfigurators) {
            configurator.loadDefaultValues();
        }
    }

    @EventListener("config.changed")
    public void synch() {
        synchWithoutSave();
        if (config.hasChanged()) config.save();
    }

    private void synchWithoutSave() {
        for (ConfigField configField : configFields)
            configField.loadValue(config);
        for (Configurator configurator : subConfigurators)
            configurator.synchWithoutSave();
    }

    public Configuration getConfiguration() {
        return config;
    }

    public ConfigValueType getConfigValueType(String configValueName) {
        for (ConfigField configField : configFields)
            if (configField.name.endsWith(configValueName)) return configField.type;
        return null;
    }

    public static enum ConfigValueType {
        BOOLEAN, INTEGER, DOUBLE, STRING, BLOCK;

        public static ConfigValueType forClass(Class<?> type) {
            if (PrimitiveUtil.BOOLEAN.equals(type)) {
                return BOOLEAN;
            } else if (PrimitiveUtil.INTEGER.equals(type)) {
                return INTEGER;
            } else if (PrimitiveUtil.DOUBLE.equals(type)) {
                return DOUBLE;
            } else if (type == String.class) {
                return STRING;
            } else if (type == Block.class) {
                return BLOCK;
            }
            return null;
        }
    }

    static class ConfigField {
        private Field field;
        private ConfigValueType type;
        private Object defaultValue = null;
        private String fieldName, name, group;

        public ConfigField(Field field, ConfigValueType type) {
            this.field = field;
            this.type = type;
            try {
                defaultValue = field.get(null);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            fieldName = field.getName();
            String[] splitName = fieldName.split("_");
            name = splitName[splitName.length - 1];
            group = splitName.length > 1 ? splitName[0] : Configuration.CATEGORY_GENERAL;
        }

        public void setValue(Object value) {
            try {
                field.set(null, value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public void loadValue(Configuration config) {
            switch (type) {
            case BOOLEAN:
                setValue(config.get(group, name, (Boolean) defaultValue).getBoolean((Boolean) defaultValue));
                break;
            case INTEGER:
                setValue(config.get(group, name, (Integer) defaultValue).getInt());
                break;
            case DOUBLE:
                setValue(config.get(group, name, (Double) defaultValue).getDouble((Double) defaultValue));
                break;
            case STRING:
                setValue(config.get(group, name, (String) defaultValue).getString());
                break;
            case BLOCK:
                String defaultBlockName = ((Block) defaultValue).getUnlocalizedName();
                Property configProperty = config.get(group, name, defaultBlockName);
                Block loadedBlock = UnlocalizedNameUtil.getBlockByUnlocalizedName(configProperty.getString());
                if (loadedBlock == null) {
                    loadedBlock = (Block) defaultValue;
                    configProperty.set(defaultBlockName);
                }
                setValue(loadedBlock);
            }
        }
    }
}
