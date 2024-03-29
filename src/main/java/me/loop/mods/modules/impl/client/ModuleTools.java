package me.loop.mods.modules.impl.client;

import me.loop.api.utils.impl.renders.TextUtil;
import me.loop.mods.modules.Category;
import me.loop.mods.modules.Module;
import me.loop.mods.settings.Setting;

public class ModuleTools extends Module {
    private static ModuleTools INSTANCE;
    public Setting<Notifier> notifier = add(new Setting<>("ModuleNotifier", Notifier.INFINITYLOOP));
    public Setting<PopNotifier> popNotifier = add(new Setting<>("PopNotifier", PopNotifier.NONE));
    public Setting<TextUtil.Color> abyssColor = this.add(new Setting<>("AbyssTextColor", TextUtil.Color.AQUA, color -> this.notifier.getValue() == Notifier.ABYSS));

    public ModuleTools() {
        super("ModuleTools", "Change settings", Category.CLIENT, true, false);
        INSTANCE = this;
    }


    public static ModuleTools getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModuleTools();
        }
        return INSTANCE;
    }

    public enum Notifier {
        INFINITYLOOP,
        FUTURE,
        DOTGOD,
        MUFFIN,
        WEATHER,
        SNOW,
        PYRO,
        CATALYST,
        KONAS,
        RUSHERHACK,
        LEGACY,
        EUROPA,
        ABYSS,
        LUIGIHACK;
    }

    public enum PopNotifier {
        PHOBOS,
        FUTURE,
        DOTGOD,
        NONE;
    }
}