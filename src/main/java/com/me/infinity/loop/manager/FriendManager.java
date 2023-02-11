package com.me.infinity.loop.manager;

import com.me.infinity.loop.features.Feature;
import com.me.infinity.loop.features.command.Command;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.LoopUtil;
import com.me.infinity.loop.util.player.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FriendManager
        extends Feature {
    public static List<String> friends = new ArrayList<>();

    public FriendManager() {
        super("Friends");
    }

    public boolean isFriend(String name) {
        return this.friends.stream().anyMatch(friend -> friend.equalsIgnoreCase(name));
    }

    public boolean isFriend(EntityPlayer player) {
        return this.isFriend(player.getName());
    }
    public boolean isEnemy(EntityPlayer player) {
        return false;
    }


    public void removeFriend(String name) {
        friends.remove(name);
    }




    public static void loadFriends(){
        try {
            File file = new File("loop/misc/friends.txt");

            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    while (reader.ready()) {
                        friends.add(reader.readLine());
                    }

                }
            }
        } catch (Exception ignored) {}
    }

    public static void saveFriends() {
        File file = new File("loop/misc/friends.txt");
        try {
            file.createNewFile();
        } catch (Exception ignored){

        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String friend : friends) {
                writer.write(friend + "\n");
            }
        } catch (Exception ignored){}
    }


    public List<String> getFriends() {
        return this.friends;
    }

    public void addFriend(String friend) {
        this.friends.add(friend);
        try {
            LoopUtil.saveUserAvatar("https://minotar.net/helm/" + friend + "/100.png", friend);
        } catch (Exception e){
            Command.sendMessage("Failed to load skin!");
        }
    }


    public void clear() {
        friends.clear();
    }
}