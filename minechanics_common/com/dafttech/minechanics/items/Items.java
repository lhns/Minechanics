package com.dafttech.minechanics.items;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;

public class Items {
    public static final Items instance = new Items();

    @EventListener("INIT")
    public void registerBlocks(Event event) {

    }
}
