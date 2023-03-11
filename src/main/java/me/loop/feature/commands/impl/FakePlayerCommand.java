package me.loop.feature.commands.impl;

import me.loop.feature.commands.Command;
import me.loop.feature.modules.impl.player.FakePlayer;

public class FakePlayerCommand extends Command {

    public FakePlayerCommand() { super("fakeplayer"); }

    @Override
    public void execute(String[] commands) {
        FakePlayer.getInstance().enable();
    }
}
