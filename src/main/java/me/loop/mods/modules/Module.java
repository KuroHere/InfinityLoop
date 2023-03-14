package me.loop.mods.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.events.impl.render.Render2DEvent;
import me.loop.api.events.impl.render.Render3DEvent;
import me.loop.api.managers.Managers;
import me.loop.mods.Mod;
import me.loop.mods.commands.Command;
import me.loop.mods.settings.Bind;
import me.loop.mods.settings.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class Module
        extends Mod {

    public Setting<Boolean> enabled = this.add(new Setting<Boolean>("Enabled", false));
    public Setting<Boolean> drawn = this.add(new Setting<Boolean>("Drawn", true));
    public Setting<Bind> bind = this.add(new Setting<Bind>("Bind", new Bind(-1)));

    //Main stuff
    private final String description;
    private final Category category;
    private final boolean shouldListen;
    public Setting<String> displayName;
    public String hudInfo;

    // Misc
    public float arrayListOffset = 0.0f;
    public float arrayListVOffset = 0.0f;
    public float offset;
    public boolean sliding;

    //General stuff
    public Module(String name, String description, Category category, boolean listen) {
        super(name);

        //this.displayName = this.add(new Setting<String>("DisplayName", name));
        this.description = description;
        this.category = category;
        shouldListen = listen;
    }

    public Module(String name, String description, Category category) {
        super(name);

        this.description = description;
        this.category = category;
        shouldListen = false;
    }

    public void enable() {
        enabled.setValue(true);
        this.onToggle();
        onEnable();

        Command.sendMessageWithID(ChatFormatting.DARK_AQUA + getName() + "\u00a7r" + ".enabled =" + "\u00a7r" + ChatFormatting.GREEN + " true.", hashCode());

        if (this.isOn() && shouldListen) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void disable() {
        enabled.setValue(false);
        onDisable();
        this.onDisable();

        Command.sendMessageWithID(ChatFormatting.DARK_AQUA + getName() + "\u00a7r" + ".enabled =" + "\u00a7r" + ChatFormatting.RED + " false.", hashCode());

        if (this.shouldListen) {
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
        Module module = Managers.moduleManager.getModuleByDisplayName(name);
        Module originalModule = Managers.moduleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", Original name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage("\u00a7cA module of this name already exists.");
    }
}
