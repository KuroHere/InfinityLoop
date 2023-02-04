package com.me.infinity.loop.features.command.commands;

import com.me.infinity.loop.Loop;
import com.me.infinity.loop.features.command.Command;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Loop.unload(true);
    }
}

