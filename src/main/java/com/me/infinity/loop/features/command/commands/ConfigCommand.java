package com.me.infinity.loop.features.command.commands;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.manager.ConfigManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.me.infinity.loop.features.command.Command;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config");
    }

    public void execute(String[] commands) {
        if (commands.length == 1) {
            sendMessage("configs are saved in  loop/configs/");
            return;
        }
        if (commands.length == 2)
            if ("list".equals(commands[0])) {
                StringBuilder configs = new StringBuilder("Configs: ");
                for(String str : Objects.requireNonNull(ConfigManager.getConfigList())){
                    configs.append("\n- ").append(str);
                }
                sendMessage(configs.toString());
            } else if( "dir".equals(commands[0]) ){
                try {
                    Desktop.getDesktop().browse(new File("loop/configs/").toURI());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sendMessage("There is no such command!... Maybe list ?");
            }
        if (commands.length >= 3) {
            switch (commands[0]) {
                case "save":
                case "create":
                    ConfigManager.save(commands[1]);
                    return;
                case "set":
                case "load":
                    ConfigManager.load(commands[1]);
                    return;
            }
            sendMessage("\n" + "There is no such command! Usage example: <save/load>");
        }
    }

}