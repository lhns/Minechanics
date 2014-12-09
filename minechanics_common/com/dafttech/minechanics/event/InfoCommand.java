package com.dafttech.minechanics.event;

import net.minecraft.command.ICommandSender;

public class InfoCommand {
    public ICommandSender sender;
    public String[] command;

    public InfoCommand(ICommandSender sender, String[] command) {
        this.sender = sender;
        this.command = command;
    }
}
