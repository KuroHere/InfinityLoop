package me.loop.mods.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.InfinityLoop;
import me.loop.mods.commands.Command;

public class FriendCommand
        extends Command {
    public FriendCommand() {
        super("friend", new String[]{"<add/del/name/clear>", "<name>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            if (InfinityLoop.friendManager.getFriends().isEmpty()) {
                FriendCommand.sendMessage("Friend list empty D:.");
            } else {
                String f = "Friends: ";
                for (String friend : InfinityLoop.friendManager.getFriends()) {
                    try {
                        f = f + friend + ", ";
                    } catch (Exception exception) {
                    }
                }
                FriendCommand.sendMessage(f);
            }
            return;
        }
        if (commands.length == 2) {
            switch (commands[0]) {
                case "reset": {
                    InfinityLoop.friendManager.clear();
                    FriendCommand.sendMessage("Friends got reset.");
                    return;
                }
            }
            FriendCommand.sendMessage(commands[0] + (InfinityLoop.friendManager.isFriend(commands[0]) ? " is friended." : " isn't friended."));
            return;
        }
        if (commands.length >= 2) {
            switch (commands[0]) {
                case "add": {
                    InfinityLoop.friendManager.addFriend(commands[1]);
                    FriendCommand.sendMessage(ChatFormatting.GREEN + commands[1] + " has been friended");
                    mc.player.sendChatMessage("/w "+ commands[1] + " i friended u at InfinityLoop Client");
                    return;
                }
                case "del": {
                    InfinityLoop.friendManager.removeFriend(commands[1]);
                    FriendCommand.sendMessage(ChatFormatting.RED + commands[1] + " has been unfriended");
                    return;
                }
            }
            FriendCommand.sendMessage("Unknown Command, try friend add/del (name)");
        }
    }
}

