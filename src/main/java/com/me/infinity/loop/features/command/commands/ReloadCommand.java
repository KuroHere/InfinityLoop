package com.me.infinity.loop.features.command.commands;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.command.Command;

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

