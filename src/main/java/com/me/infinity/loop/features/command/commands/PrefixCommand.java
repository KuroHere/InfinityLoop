package com.me.infinity.loop.features.command.commands;

import com.me.infinity.loop.InfinityLoop;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.me.infinity.loop.features.command.Command;

public class PrefixCommand
        extends Command {
    public PrefixCommand() {
        super("prefix", new String[]{"<char>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(ChatFormatting.GREEN + "Current prefix is " + InfinityLoop.commandManager.getPrefix());
            return;
        }
        InfinityLoop.commandManager.setPrefix(commands[0]);
        Command.sendMessage("Prefix changed to " + ChatFormatting.GRAY + commands[0]);
    }
}

