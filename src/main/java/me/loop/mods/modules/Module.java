package me.loop.mods.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.InfinityLoop;
import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.events.impl.render.Render2DEvent;
import me.loop.api.events.impl.render.Render3DEvent;
import me.loop.mods.Mod;
import me.loop.mods.commands.Command;
import me.loop.mods.settings.Bind;
import me.loop.mods.settings.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class Module
        extends Mod {

    public Setting<Boolean> enabled = this.add(new Setting<>("Enabled", false));
    public Setting<Boolean> drawn = this.add(new Setting<>("Drawn", true));
    public Setting<Bind> bind = this.add(new Setting<>("Bind", new Bind(-1)));

    //Main stuff
    private final String description;
    private final Category category;
    private final boolean shouldListen;
    public boolean hidden;
    public Setting<String> displayName;
    public String hudInfo;

    // Misc
    public float arrayListOffset = 0.0f;
    public float arrayListVOffset = 0.0f;
    public float offset;
    public boolean sliding;

    //General stuff
    public Module(String name, String description, Category category, boolean listen, boolean hidden) {
        super(name);

        this.displayName = this.add(new Setting<>("DisplayName", name));
        this.description = description;
        this.category = category;
        shouldListen = listen;
        this.hidden = hidden;
    }

    public Module(String name, String description, Category category) {
        super(name);

        this.displayName = this.add(new Setting<>("DisplayName", name));
        this.description = description;
        this.category = category;
        shouldListen = false;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public void enable() {
        this.enabled.setValue(true);
        this.onToggle();
        this.onEnable();
        if (this.isOn() && this.shouldListen) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void disable() {
        if (this.shouldListen) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
        this.enabled.setValue(false);
        this.onToggle();
        this.onDisable();
    }

    public void toggle() {
        ClientEvent event = new ClientEvent(!this.isEnabled() ? 1 : 0, this);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            this.setEnabled(!this.isEnabled());
        }
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        return this.enabled.getValue() == false;
    }


    public boolean isDrawn() {
        return drawn.getValue();
    }

    public boolean isListening() {
        return shouldListen && isOn();
    }

    //Override-able methods



    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
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

    public void onTotemPop(EntityPlayer player) {
    }

    public void onDeath(EntityPlayer player) {
    }

    public void onUnload() {
    }

    //Getters

    public String getArrayListInfo() {
        return getName()
                + ChatFormatting.GRAY
                + (getInfo() != null
                ? " ["
                + ChatFormatting.WHITE
                + getInfo()
                + ChatFormatting.GRAY
                + "]"
                : "");
    }
    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public String getDisplayInfo() {
        return null;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isSliding() {
        return this.sliding;
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

    public String getFullArrayString() {
        return this.getDisplayName() + "\u00a78" + (this.getDisplayInfo() != null ? " [\u00a7r" + this.getDisplayInfo() + "\u00a78" + "]" : "");
    }

    public void onClientTick(final TickEvent.ClientTickEvent event) {
    }

    public void onMotionUpdate() {
    }

    //Setters

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    public void setHudInfo(String info) {
        this.hudInfo = info;
    }

    public void setDisplayName(String name) {
        Module module = InfinityLoop.moduleManager.getModuleByDisplayName(name);
        Module originalModule = InfinityLoop.moduleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", Original name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage("\u00a7cA module of this name already exists.");
    }
}
