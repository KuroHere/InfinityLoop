package me.loop.mods.commands.impl;


import me.loop.InfinityLoop;
import me.loop.mods.commands.Command;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload");
    }

    @Override
    public void execute(String[] commands) {
        InfinityLoop.reload();
    }
}
