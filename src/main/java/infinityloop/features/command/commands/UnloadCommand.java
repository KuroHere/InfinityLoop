package infinityloop.features.command.commands;

import infinityloop.InfinityLoop;
import infinityloop.features.command.Command;

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

