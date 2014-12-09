package com.dafttech.minechanics.network;

import net.minecraft.command.CommandBase;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;
import com.dafttech.minechanics.blocks.water.WaterMechanics;
import com.dafttech.minechanics.data.ModConfig;
import com.dafttech.minechanics.event.InfoCommand;

public class StandardCommandHandler {
    public static StandardCommandHandler instance = new StandardCommandHandler();

    @EventListener("RECEIVECOMMAND")
    public void onReceiveCommand(Event event) {
        InfoCommand info = event.getInput(0, InfoCommand.class);
        int extra;
        if (info.command[0].equals("water")) {
            if (info.command[1].equals("mechanics")) {
                ModConfig.waterMechanics = !ModConfig.waterMechanics;
                event.addOutput("water mechanics switched " + String.valueOf(ModConfig.waterMechanics));
            } else if (info.command[1].equals("log")) {
                ModConfig.tickLogInfo = !ModConfig.tickLogInfo;
                event.addOutput("water log info switched " + String.valueOf(ModConfig.tickLogInfo));
            } else if (info.command[1].equals("fast")) {
                ModConfig.fluidFastMode = !ModConfig.fluidFastMode;
                event.addOutput("fast water switched " + String.valueOf(ModConfig.fluidFastMode));
            } else if (info.command[1].equals("removeover")) {
                extra = CommandBase.parseIntWithMin(info.sender, info.command[2], 0);
                WaterMechanics.removeWaterOver = extra;
                event.addOutput("removing water over y=" + String.valueOf(extra));
            } else if (info.command[1].equals("fillunder")) {
                extra = CommandBase.parseIntWithMin(info.sender, info.command[2], 0);
                WaterMechanics.fillWaterUnder = extra;
                event.addOutput("filling water under y=" + String.valueOf(extra));
            }
        } else if (info.command[0].equals("r")) {
            if (info.command.length == 1) {
                event.addOutput("reset");
            } else {
            }

        }
    }
}
