package me.loop.features.command.commands;

import me.loop.InfinityLoop;
import me.loop.features.command.Command;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        InfinityLoop.unload(true);
    }
}

