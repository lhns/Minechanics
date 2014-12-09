package org.lolhens.minechanics.core.config.gui.label;

import java.util.List;
import java.util.regex.Pattern;

import net.minecraftforge.common.config.ConfigCategory;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.GuiEditArrayEntries.IArrayEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class LabelConfigElement implements IConfigElement<Object> {

    private ConfigCategory ctgy;
    private String value;

    public LabelConfigElement(ConfigCategory ctgy, String value) {
        this.ctgy = ctgy;
        this.value = value;
    }

    @Override
    public boolean isProperty() {
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends IConfigEntry> getConfigEntryClass() {
        return LabelConfigEntry.class;
    }

    @Override
    public Class<? extends IArrayEntry> getArrayEntryClass() {
        return null;
    }

    @Override
    public String getName() {
        return value;
    }

    @Override
    public String getQualifiedName() {
        return null;
    }

    @Override
    public String getLanguageKey() {
        return ctgy.getLanguagekey();
    }

    @Override
    public String getComment() {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<IConfigElement> getChildElements() {
        return null;
    }

    @Override
    public ConfigGuiType getType() {
        return null;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isListLengthFixed() {
        return true;
    }

    @Override
    public int getMaxListLength() {
        return 0;
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public Object getDefault() {
        return null;
    }

    @Override
    public Object[] getDefaults() {
        return null;
    }

    @Override
    public void setToDefault() {
    }

    @Override
    public boolean requiresWorldRestart() {
        return false;
    }

    @Override
    public boolean showInGui() {
        return true;
    }

    @Override
    public boolean requiresMcRestart() {
        return false;
    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public Object[] getList() {
        return null;
    }

    @Override
    public String[] getValidValues() {
        return null;
    }

    @Override
    public Object getMinValue() {
        return null;
    }

    @Override
    public Object getMaxValue() {
        return null;
    }

    @Override
    public Pattern getValidationPattern() {
        return null;
    }

    @Override
    public void set(Object arg0) {
    }

    @Override
    public void set(Object[] arg0) {
    }

}
