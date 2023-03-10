package me.loop.client.modules;

import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.events.impl.render.Render2DEvent;
import me.loop.api.events.impl.render.Render3DEvent;
import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.Util;
import me.loop.client.Client;
import me.loop.client.commands.Command;
import me.loop.client.modules.impl.client.HUD;
import me.loop.client.modules.settings.Setting;
import me.loop.client.modules.settings.impl.Bind;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Module
        extends Client {

    private final Category category;
    private final String description;
    public Setting<Boolean> enabled = this.add(new Setting<Boolean>("Enabled", false));
    public Setting<Boolean> drawn = this.add(new Setting<Boolean>("Drawn", true));
    public Setting<Bind> bind = this.add(new Setting<Bind>("Bind", new Bind(-1)));
    public Setting<String> displayName;
    public String hudInfo;
    public boolean hasListener;
    public boolean alwaysListening;
    public boolean hidden;
    public float arrayListOffset = 0.0f;
    public float arrayListVOffset = 0.0f;
    public float offset;

    public float vOffset;
    public boolean sliding;
    public Animation animation;

    public Module(String name, String description, Category category, boolean hasListener, boolean hidden, boolean alwaysListening) {
        super(name);
        this.displayName = this.add(new Setting<String>("DisplayName", name));
        this.description = description;
        this.category = category;
        this.hasListener = hasListener;
        this.hidden = hidden;
        this.alwaysListening = alwaysListening;
        this.animation = new Animation(this);
    }

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

    public void onUnload() {
    }

    public String getDisplayInfo() {
        return null;
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        return this.enabled.getValue() == false;
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

        //Command.sendMessageWithID(ChatFormatting.DARK_AQUA + getName() + "\u00a7r" + "enabled =" + "\u00a7r" + ChatFormatting.GREEN + " true", hashCode());

        if (this.isOn() && this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void disable() {
        if (this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
        this.enabled.setValue(false);
        this.onToggle();
        this.onDisable();

        //Command.sendMessageWithID(ChatFormatting.DARK_AQUA + getName() + "\u00a7r" + "enabled =" + "\u00a7r" + ChatFormatting.RED + " false", hashCode());

    }

    public void toggle() {
        ClientEvent event = new ClientEvent(!this.isEnabled() ? 1 : 0, this);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            this.setEnabled(!this.isEnabled());
        }
    }

    public String getDisplayName() {
        return this.displayName.getValue();
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

    public String getDescription() {
        return this.description;
    }

    public boolean isSliding() {
        return this.sliding;
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
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

    public boolean listening() {
        return this.hasListener && this.isOn() || this.alwaysListening;
    }

    public String getFullArrayString() {
        return this.getDisplayName() + "\u00a78" + (this.getDisplayInfo() != null ? " [\u00a7r" + this.getDisplayInfo() + "\u00a78" + "]" : "");
    }

    public void setUndrawn() {
        this.drawn.setValue(null);
    }

    public void onClientTick(final TickEvent.ClientTickEvent event) {
    }

    public void setHudInfo(String info) {
        this.hudInfo = info;
    }

    public void onMotionUpdate() {
    }

    public class Animation
            extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        public String lastText;
        public boolean shouldMetaSlide;
        ScheduledExecutorService service;

        public Animation(Module module) {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
            this.module = module;
        }

        @Override
        public void run() {
            String text = this.module.getDisplayName() + "\u00a77" + (this.module.getDisplayInfo() != null ? " [\u00a7f" + this.module.getDisplayInfo() + "\u00a77" + "]" : "");
            this.module.offset = (float) Module.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
            this.module.vOffset = (float) Module.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
            if (this.module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                if (this.module.arrayListOffset > this.module.offset && Util.mc.world != null) {
                    this.module.arrayListOffset -= this.module.offset;
                    this.module.sliding = true;
                }
            } else if (this.module.isDisabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                if (this.module.arrayListOffset < (float) Module.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                    this.module.arrayListOffset += this.module.offset;
                    this.module.sliding = true;
                } else {
                    this.module.sliding = false;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread for " + this.module.getName());
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}
