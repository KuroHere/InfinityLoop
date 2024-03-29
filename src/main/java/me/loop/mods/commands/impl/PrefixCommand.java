package me.loop.mods.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.InfinityLoop;
import me.loop.mods.commands.Command;

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

