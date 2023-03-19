package me.loop.features.command.commands;

import me.loop.features.command.Command;
import me.loop.InfinityLoop;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        InfinityLoop.reload();
    }
}

