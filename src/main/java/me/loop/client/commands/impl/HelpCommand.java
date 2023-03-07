package me.loop.client.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.api.managers.Managers;
import me.loop.client.commands.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : Managers.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + Managers.commandManager.getPrefix() + command.getName());
        }
    }
}

