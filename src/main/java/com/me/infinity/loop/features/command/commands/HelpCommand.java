package com.me.infinity.loop.features.command.commands;

import com.me.infinity.loop.Loop;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.me.infinity.loop.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : Loop.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + Loop.commandManager.getPrefix() + command.getName());
        }
    }
}

