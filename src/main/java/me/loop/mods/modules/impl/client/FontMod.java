package me.loop.mods.modules.impl.client;

import me.loop.InfinityLoop;
import me.loop.api.events.impl.client.ClientEvent;
import me.loop.mods.commands.Command;
import me.loop.mods.modules.Category;
import me.loop.mods.modules.Module;
import me.loop.mods.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class FontMod
        extends Module {

    public static FontMod INSTANCE;
    public Setting<String> fontName = this.add(new Setting<String>("FontName", "Arial", "Name of the font."));
    public Setting<Integer> fontSize = this.add(new Setting<Integer>("Size", Integer.valueOf(18), Integer.valueOf(12), Integer.valueOf(30), "Size of the font."));
    public Setting<Integer> fontStyle = this.add(new Setting<Integer>("Style", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(3), "Style of the font."));
    public Setting<Boolean> antiAlias = this.add(new Setting<Boolean>("AntiAlias", Boolean.valueOf(true), "Smoother font."));
    public Setting<Boolean> fractionalMetrics = this.add(new Setting<Boolean>("Metrics", Boolean.valueOf(true), "Thinner font."));
    public Setting<Boolean> shadow = this.add(new Setting<Boolean>("Shadow", Boolean.valueOf(true), "Less shadow offset font."));
    public Setting<Boolean> showFonts = this.add(new Setting<Boolean>("Fonts", Boolean.valueOf(false), "Shows all fonts."));
    public Setting<Boolean> full = this.add(new Setting<Boolean>("Full", true));
    private boolean reloadFont = false;

    public FontMod() {
        super("CustomFont", "CustomFont for all of the clients text. Use the font command.", Category.CLIENT, true, false);
        INSTANCE = this;
    }

    public static boolean checkFont(String font, boolean message) {
        String[] fonts;
        for (String s : fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if (!message && s.equals(font)) {
                return true;
            }
            if (!message) continue;
            Command.sendMessage(s);
        }
        return false;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        Setting setting;
        if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getMod().equals(this)) {
            if (setting.getName().equals("FontName") && !FontMod.checkFont(setting.getPlannedValue().toString(), false)) {
                Command.sendMessage("\u00a7cThat font doesnt exist.");
                event.setCanceled(true);
                return;
            }
            this.reloadFont = true;
        }
    }

    @Override
    public void onTick() {
        if (this.showFonts.getValue().booleanValue()) {
            FontMod.checkFont("Hello", true);
            Command.sendMessage("Current Font: " + this.fontName.getValue());
            this.showFonts.setValue(false);
        }
        if (this.reloadFont) {
            InfinityLoop.textManager.init(false);
            this.reloadFont = false;
        }
    }
}