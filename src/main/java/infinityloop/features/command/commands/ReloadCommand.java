package infinityloop.features.command.commands;

import infinityloop.InfinityLoop;
import infinityloop.features.command.Command;

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
