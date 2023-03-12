package me.loop.client.commands.impl;

import me.loop.client.commands.Command;
import me.loop.client.modules.impl.player.FakePlayer;

public class FakePlayerCommand extends Command {

    public FakePlayerCommand() { super("fakeplayer"); }

    @Override
    public void execute(String[] commands) {
        FakePlayer.getInstance().enable();
    }
}
