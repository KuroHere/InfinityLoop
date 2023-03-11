package me.loop.feature.commands.impl;

import me.loop.feature.commands.Command;
import me.loop.api.utils.impl.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.lang.reflect.Field;

public class loginCommand extends Command {

    public loginCommand() {
        super("login");
    }

    @Override
    public void execute(String[] var1) {
        try {
            login(var1[0]);
            Command.sendMessage("\n" + "Account changed to: " + Util.mc.getSession().getUsername());
        }
        catch (Exception exception) {
            Command.sendMessage("Usage: .login nick");
        }
    }


    public static void login(String string) {
        try {
            Field field = Minecraft.class.getDeclaredField("field_71449_j"); //session
            field.setAccessible(true);
            Field field2 = Field.class.getDeclaredField("modifiers");
            field2.setAccessible(true);
            field2.setInt(field, field.getModifiers() & 0xFFFFFFEF);
            field.set(Util.mc, new Session(string, "", "", "mojang"));
        }
        catch (Exception exception) {
            Command.sendMessage("Wrong name! " + exception);
        }
    }

}
