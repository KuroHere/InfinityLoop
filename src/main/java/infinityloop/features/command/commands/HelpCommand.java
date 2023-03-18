package infinityloop.features.command.commands;

import infinityloop.InfinityLoop;
import com.mojang.realmsclient.gui.ChatFormatting;
import infinityloop.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : InfinityLoop.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + InfinityLoop.commandManager.getPrefix() + command.getName());
        }
    }
}

