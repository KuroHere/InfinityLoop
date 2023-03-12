package me.loop.client.commands.impl;

import me.loop.api.managers.Managers;
import me.loop.client.commands.Command;
import me.loop.api.modules.Module;

public class ToggleCommand
        extends Command {
    public ToggleCommand() {
        super("toggle", new String[]{"<toggle>", "<module>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 2) {
            String name = commands[0].replaceAll("_", " ");
            Module module = Managers.moduleManager.getModuleByName(name);
            if (module != null) {
                module.toggle();
            } else {
                Command.sendMessage("Unable to find a module with that name!");
            }
        } else {
            Command.sendMessage("Please provide a valid module name!");
        }
    }
}
