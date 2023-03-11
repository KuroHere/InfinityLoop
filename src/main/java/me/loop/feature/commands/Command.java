package me.loop.feature.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.api.managers.Managers;
import me.loop.feature.Feature;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command
        extends Feature {
    String specialUsage;
    String description;
    protected String name;
    protected String[] commands;

    public Command(String name) {
        super(name);
        this.name = name;
        this.commands = new String[]{""};
    }

    public Command(String name, String[] commands) {
        super(name);
        this.name = name;
        this.commands = commands;
    }

    public Command(String name, String specialUsage, String description) {
        this.name = name;
        this.specialUsage = specialUsage;
        this.description = description;
    }

    public static void sendMessageWithID(String message, int id) {
        if (!nullCheck()) {
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatMessage(Managers.commandManager.getPrefix() + ChatFormatting.GRAY + message), id);
        }
    }

    public static void sendOverwriteMessage(String message, int id, boolean notification) {
        TextComponentString component = new TextComponentString(message);
        Command.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, id);
        if (notification) {
            Managers.notificationManager.addNotification(message, 3000L);
        }
    }

    public static void sendMessage(String message, boolean notification) {
        Command.sendSilentMessage(Managers.commandManager.getClientMessage() + " " + "\u00a7r" + message);
        if (notification) {
            Managers.notificationManager.addNotification(message, 3000L);
        }
    }


    public String getDescription() {
        return this.description;
    }

    public String getUsageException() {
        return this.specialUsage;
    }


    public static void sendMessage(String message) {
        Command.sendSilentMessage(Managers.commandManager.getClientMessage() + " " + ChatFormatting.GRAY + message);
    }

    public static void sendSilentMessage(String message) {
        if (Command.nullCheck()) {
            return;
        }
        Command.mc.player.sendMessage(new ChatMessage(message));
    }

    public static String getCommandPrefix() {
        return Managers.commandManager.getPrefix();
    }

    public abstract void execute(String[] var1);

    @Override
    public String getName() {
        return this.name;
    }

    public String[] getCommands() {
        return this.commands;
    }

    public static class ChatMessage
            extends TextComponentBase {
        private final String text;

        public ChatMessage(String text) {
            Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher matcher = pattern.matcher(text);
            StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                String replacement = matcher.group().substring(1);
                matcher.appendReplacement(stringBuffer, replacement);
            }
            matcher.appendTail(stringBuffer);
            this.text = stringBuffer.toString();
        }

        public String getUnformattedComponentText() {
            return this.text;
        }

        public ITextComponent createCopy() {
            return null;
        }

        public ITextComponent shallowCopy() {
            return new ChatMessage(this.text);
        }
    }
}

