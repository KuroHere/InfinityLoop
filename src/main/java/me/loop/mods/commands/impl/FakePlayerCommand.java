package me.loop.mods.commands.impl;

import me.loop.mods.commands.Command;
import me.loop.mods.modules.impl.player.FakePlayer;

public class FakePlayerCommand extends Command {

    public FakePlayerCommand() { super("fakeplayer"); }

    @Override
    public void execute(String[] commands) {
        FakePlayer.getInstance().enable();
    }
}
