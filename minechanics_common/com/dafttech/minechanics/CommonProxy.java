package com.dafttech.minechanics;

//import com.dafttech.minechanics.mechanics.ContainerToolbox;
//import com.dafttech.minechanics.mechanics.GuiToolbox;
//import com.dafttech.minechanics.mechanics.ItemToolbox;
public class CommonProxy {
    @Deprecated
    public static String sprites = "/com/dafttech/minechanics/sprites.png";
    public static String plants = "/com/dafttech/minechanics/plants.png";
    public static String gui_bg = "/com/dafttech/minechanics/gui_bg.png";
    public static String gui_bdr_hr = "/com/dafttech/minechanics/gui_bdr_hr.png";
    public static String gui_bdr_vr = "/com/dafttech/minechanics/gui_bdr_vr.png";
    public static String gui_edge = "/com/dafttech/minechanics/gui_edge.png";
    public static String gui_slot = "/com/dafttech/minechanics/gui_slot.png";
    public static String overlay_plant = "/com/dafttech/minechanics/plantoverlay.png";
    public static String overlay_raindrop = "/com/dafttech/minechanics/raindrop.png";

    public boolean isClient() {
        return false;
    }

    public void registerRenderers() {
    }
}
