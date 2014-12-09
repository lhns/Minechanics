package com.dafttech.minechanics.network;

import java.util.ArrayList;
import java.util.List;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.data.ModInfo;
import com.dafttech.minechanics.event.Events;

public class ChannelManager {
    public static ChannelManager instance = new ChannelManager();

    private List<String> channels = new ArrayList<String>();

    public int getChannelId(String name) {
        if (!channels.contains(name)) return -1;
        return channels.indexOf(name);
    }

    public String getChannel(int id) {
        if (id < 0 || id >= channels.size()) return ModInfo.MOD_CHANNEL;
        return channels.get(id);
    }

    public void registerChannel(String name) {
        channels.add(name);
    }

    @EventListener("INIT")
    public void registerChannels(Event event) {
        Events.EVENTMANAGER.callSync(Events.EVENT_REGISTERCHANNELS);
    }
}
