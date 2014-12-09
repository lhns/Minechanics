package org.lolhens.minechanics.core.item;

import org.lolhens.minechanics.tools.Toolbox;

import com.dafttech.eventmanager.EventListener;

public class Items {
    public static ItemBase toolbox = new Toolbox("toolbox");

    @EventListener("init")
    public static void init() {
    }
}
