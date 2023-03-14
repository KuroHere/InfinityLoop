package me.loop.mods.commands.impl;

import me.loop.InfinityLoop;
import me.loop.mods.commands.Command;

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

