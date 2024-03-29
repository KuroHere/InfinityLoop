package me.loop.api.managers;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.InfinityLoop;
import me.loop.mods.Mod;
import me.loop.mods.commands.Command;
import me.loop.mods.modules.impl.client.ModuleTools;
import me.loop.mods.modules.impl.misc.Notifications;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TotemPopManager
        extends Mod {
    private final Set<EntityPlayer> toAnnounce = new HashSet<>();
    private Notifications notifications;
    private Map<EntityPlayer, Integer> poplist = new ConcurrentHashMap<>();

    public void onUpdate() {
       //if (this.notifications.totemAnnounce.passedMs(this.notifications.delay.getValue()) && this.notifications.isOn() && this.notifications.totemPops.getValue()) {
            for (EntityPlayer player : this.toAnnounce) {
                if (player == null) continue;
                int playerNumber = 0;
                for (char character : player.getName().toCharArray()) {
                    playerNumber += character;
                    playerNumber *= 10;
                }
                Command.sendOverwriteMessage(this.pop(player), playerNumber, this.notifications.totemNoti.getValue());
                this.toAnnounce.remove(player);
                this.notifications.totemAnnounce.reset();
                break;
            }
        //}
    }

    public String pop(EntityPlayer player) {
        if (this.getTotemPops(player) == 1) {
            if (ModuleTools.getInstance().isEnabled()) {
                switch (ModuleTools.getInstance().popNotifier.getValue()) {
                    case FUTURE: {
                        String text = ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.getName() + ChatFormatting.GRAY + " just popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.GRAY + " totem.";
                        return text;
                    }
                    case PHOBOS: {
                        String text = ChatFormatting.GOLD + player.getName() + ChatFormatting.RED + " popped " + ChatFormatting.GOLD + this.getTotemPops(player) + ChatFormatting.RED + " totem.";
                        return text;
                    }
                    case DOTGOD: {
                        String text = ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.LIGHT_PURPLE + player.getName() + " has popped " + ChatFormatting.RED + this.getTotemPops(player) + ChatFormatting.LIGHT_PURPLE + " time in total!";
                        return text;
                    }
                    case NONE: {
                        return InfinityLoop.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + player.getName() + " popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totem.";
                    }
                }
            } else {
                return InfinityLoop.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + player.getName() + " popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totem.";
            }
        } else {
            if (ModuleTools.getInstance().isEnabled()) {
                switch (ModuleTools.getInstance().popNotifier.getValue()) {
                    case FUTURE: {
                        String text = ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.getName() + ChatFormatting.GRAY + " just popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.GRAY + " totems.";
                        return text;
                    }
                    case PHOBOS: {
                        String text = ChatFormatting.GOLD + player.getName() + ChatFormatting.RED + " popped " + ChatFormatting.GOLD + this.getTotemPops(player) + ChatFormatting.RED + " totems.";
                        return text;
                    }
                    case DOTGOD: {
                        String text = ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.LIGHT_PURPLE + player.getName() + " has popped " + ChatFormatting.RED + this.getTotemPops(player) + ChatFormatting.LIGHT_PURPLE + " times in total!";
                        return text;
                    }
                    case NONE: {
                        return InfinityLoop.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + player.getName() + " popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totems.";
                    }
                }
            } else {
                return InfinityLoop.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + player.getName() + " popped " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totems.";
            }
        }
        return "";
    }


    public void onLogout() {
        this.onOwnLogout(this.notifications.clearOnLogout.getValue());
    }

    public void init() {
        this.notifications = InfinityLoop.moduleManager.getModuleByClass(Notifications.class);
    }

    public void onTotemPop(EntityPlayer player) {
        this.popTotem(player);
        if (!player.equals(TotemPopManager.mc.player)) {
            this.toAnnounce.add(player);
            this.notifications.totemAnnounce.reset();
        }
    }

    public String death1(EntityPlayer player) {
        if (this.getTotemPops(player) == 1) {
            if (ModuleTools.getInstance().isEnabled()) {
                switch (ModuleTools.getInstance().popNotifier.getValue()) {
                    case FUTURE: {
                        String text = ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.getName() + ChatFormatting.GRAY + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.GRAY + " totem.";
                        return text;
                    }
                    case PHOBOS: {
                        String text = ChatFormatting.GOLD + player.getName() + ChatFormatting.RED + " died after popping " + ChatFormatting.GOLD + this.getTotemPops(player) + ChatFormatting.RED + " totem.";
                        return text;
                    }
                    case DOTGOD: {
                        String text = ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.LIGHT_PURPLE + player.getName() + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.LIGHT_PURPLE + " time!";
                        return text;
                    }
                    case NONE: {
                        return InfinityLoop.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + player.getName() + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totem!";

                    }
                }
            } else {
                return InfinityLoop.commandManager.getClientMessage() + ChatFormatting.WHITE + player.getName() + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totem!";

            }
        } else {
            if (ModuleTools.getInstance().isEnabled()) {
                switch (ModuleTools.getInstance().popNotifier.getValue()) {
                    case FUTURE: {
                        String text = ChatFormatting.RED + "[Future] " + ChatFormatting.GREEN + player.getName() + ChatFormatting.GRAY + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.GRAY + " totems.";
                        return text;
                    }
                    case PHOBOS: {
                        String text = ChatFormatting.GOLD + player.getName() + ChatFormatting.RED + " died after popping " + ChatFormatting.GOLD + this.getTotemPops(player) + ChatFormatting.RED + " totems.";
                        return text;
                    }
                    case DOTGOD: {
                        String text = ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.LIGHT_PURPLE + player.getName() + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.LIGHT_PURPLE + " times!";
                        return text;
                    }
                    case NONE: {
                        return InfinityLoop.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + player.getName() + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totems!";

                    }
                }
            } else {
                return InfinityLoop.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + player.getName() + " died after popping " + ChatFormatting.GREEN + this.getTotemPops(player) + ChatFormatting.WHITE + " Totems!";
            }
        }
        return null;
    }


    public void onDeath(EntityPlayer player) {
        if (this.getTotemPops(player) != 0 && !player.equals(TotemPopManager.mc.player) && this.notifications.isOn() && this.notifications.totemPops.getValue()) {
            int playerNumber = 0;
            for (char character : player.getName().toCharArray()) {
                playerNumber += character;
                playerNumber *= 10;
            }
            Command.sendOverwriteMessage(this.death1(player), playerNumber, this.notifications.totemNoti.getValue());
            this.toAnnounce.remove(player);
        }
        this.resetPops(player);
    }

    public void onLogout(EntityPlayer player, boolean clearOnLogout) {
        if (clearOnLogout) {
            this.resetPops(player);
        }
    }

    public void onOwnLogout(boolean clearOnLogout) {
        if (clearOnLogout) {
            this.clearList();
        }
    }

    public void clearList() {
        this.poplist = new ConcurrentHashMap<>();
    }

    public void resetPops(EntityPlayer player) {
        this.setTotemPops(player, 0);
    }

    public void popTotem(EntityPlayer player) {
        this.poplist.merge(player, 1, Integer::sum);
    }

    public void setTotemPops(EntityPlayer player, int amount) {
        this.poplist.put(player, amount);
    }

    public int getTotemPops(EntityPlayer player) {
        Integer pops = this.poplist.get(player);
        if (pops == null) {
            return 0;
        }
        return pops;
    }

    public String getTotemPopString(EntityPlayer player) {
        return "\u00a7f" + (this.getTotemPops(player) <= 0 ? "" : "-" + this.getTotemPops(player) + " ");
    }
}
