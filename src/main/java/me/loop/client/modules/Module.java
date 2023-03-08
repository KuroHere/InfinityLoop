package me.loop.client.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.events.impl.render.Render2DEvent;
import me.loop.api.events.impl.render.Render3DEvent;
import me.loop.api.managers.Managers;
import me.loop.client.Client;
import me.loop.client.commands.Command;
import me.loop.client.modules.settings.Setting;
import me.loop.client.modules.settings.impl.Bind;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class Module
        extends Client {

    private final Category category;
    private final String description;
    public String hudInfo;
    public Setting<Boolean> enabled = this.add(new Setting<Boolean>("Enabled", false));
    public Setting<Boolean> drawn = this.add(new Setting<Boolean>("Drawn", true));
    public Setting<Bind> bind = this.add(new Setting<Bind>("Keybind", new Bind(-1)));
    public Setting<String> displayName;
    public boolean hasListener;
    public boolean alwaysListening;
    public boolean hidden;
    public float arrayListOffset = 0.0f;
    public float arrayListVOffset = 0.0f;
    public float offset;
    public float vOffset;
    public boolean sliding;

    public Module(String name, String description, Category category, boolean hasListener, boolean hidden, boolean alwaysListening) {
        super(name);
        this.displayName = this.add(new Setting<String>("DisplayName", name));
        this.description = description;
        this.category = category;
        this.hasListener = hasListener;
        this.hidden = hidden;
        this.alwaysListening = alwaysListening;
    }

    public void enable() {
        enabled.setValue(true);
        onEnable();

        Command.sendMessageWithID(ChatFormatting.DARK_AQUA + getName() + "\u00a7r" + ".enabled =" + "\u00a7r" + ChatFormatting.GREEN + " true.", hashCode());

        if (isOn() && hasListener) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void disable() {
        enabled.setValue(false);
        onDisable();

        Command.sendMessageWithID(ChatFormatting.DARK_AQUA + getName() + "\u00a7r" + ".enabled =" + "\u00a7r" + ChatFormatting.RED + " false.", hashCode());

        if (hasListener) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    public void toggle() {
        ClientEvent event = new ClientEvent(!isOn() ? 1 : 0, this);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {

            if (!isOn()) {
                enable();

            } else {
                disable();
            }
        }
    }

    public boolean isOn() {
        return enabled.getValue();
    }

    public boolean isOff() {
        return !enabled.getValue();
    }

    public boolean isDrawn() {
        return drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public boolean isListening() {
        return hasListener && isOn();
    }

    public boolean isSliding() {
        return this.sliding;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onLoad() {
    }

    public void onTick() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public void onUnload() {
    }

    public String getDisplayInfo() {
        return null;
    }


    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public void setDisplayName(String name) {
        Module module = Managers.moduleManager.getModuleByDisplayName(name);
        Module originalModule = Managers.moduleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage(ChatFormatting.RED + "A module of this name already exists.");
    }

    public String getDescription() {
        return this.description;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    public String getFullArrayString() {
        return this.getDisplayName() + ChatFormatting.GRAY + (this.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
    }

    public void setUndrawn() {
        this.drawn.setValue(null);
    }

    public void onClientTick(final TickEvent.ClientTickEvent event) {
    }

    public String getHudInfo() {
        return this.hudInfo;
    }

    public void setHudInfo(String info) {
        this.hudInfo = info;
    }

    public void onMotionUpdate() {
    }
}
