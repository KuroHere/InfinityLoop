package me.loop.api.utils.impl.minecraft;

import me.loop.client.modules.settings.Setting;
import me.loop.client.modules.settings.impl.Bind;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyBoardUtil
{
    public static boolean isKeyDown(KeyBinding binding)
    {
        return isKeyDown(binding.getKeyCode());
    }

    public static boolean isKeyDown(Setting<Bind> setting)
    {
        return isKeyDown(setting.getValue());
    }

    public static boolean isKeyDown(Bind bind)
    {
        return isKeyDown(bind.getKey());
    }

    public static boolean isKeyDown(int key)
    {
        return key != 0 && key != -1 && (key < 0 ? Mouse.isButtonDown(key + 100) : Keyboard.isKeyDown(key));
    }
}
