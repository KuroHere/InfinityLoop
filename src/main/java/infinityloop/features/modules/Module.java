package infinityloop.features.modules;

import infinityloop.InfinityLoop;
import infinityloop.event.events.render.Render2DEvent;
import infinityloop.event.events.render.Render3DEvent;
import infinityloop.event.events.render.RenderEvent;
import infinityloop.features.Feature;
import infinityloop.features.command.Command;
import infinityloop.features.modules.client.HUD;
import infinityloop.features.setting.impl.Bind;
import infinityloop.features.setting.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class Module
        extends Feature {
    private final String description;
    private final ModuleCategory category;
    public Setting<Boolean> enabled = this.register(new Setting<>("Enabled", false));
    public Setting<Boolean> drawn = this.register(new Setting<>("Drawn", true));
    public Setting<Bind> bind = this.register(new Setting<>("Keybind", new Bind(-1)));
    public Setting<String> displayName;
    public boolean toggled;
    public boolean persistent;
    public boolean hasListener;
    public boolean alwaysListening;
    public boolean hidden;
    public float arrayListOffset = 0.0f;
    public float arrayListVOffset = 0.0f;
    public float offset;
    public float vOffset;
    public boolean sliding;

    public Module(String name, String description, ModuleCategory category) {
        super(name);
        this.displayName = this.register(new Setting<>("DisplayName", name));
        this.description = description;
        this.category = category;
        this.persistent = false;
    }

    public Module(String name, String description, ModuleCategory category, boolean persistent) {
        super(name);
        this.displayName = this.register(new Setting<>("DisplayName", name));
        this.description = description;
        this.category = category;
        this.persistent = persistent;
        this.persist();
    }

    public ModuleCategory getCategory() {
        return this.category;
    }

    public boolean isSliding() {
        return this.sliding;
    }

    public void onMotionUpdate() {
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

    public void onWorldRender(RenderEvent event) {

    }
    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public void onUnload() {
    }

    public String getHudInfo() {
        return "";
    }

    public void onClientTick(final TickEvent.ClientTickEvent event) {
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public void setToggled(final boolean toggled) {
        this.toggled = toggled;
    }

    public String getDisplayInfo() {
        return null;
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        return !this.enabled.getValue();
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public void persist() {
        block0: {
            if (!this.persistent) break block0;
            this.setToggled(true);
            MinecraftForge.EVENT_BUS.register((Object)this);
        }
    }

    public void enable() {
        this.enabled.setValue(Boolean.TRUE);
        this.setToggled(true);
        this.onEnable();
        if (HUD.getInstance().notifyToggles.getValue().booleanValue()) {
            TextComponentString text = new TextComponentString(InfinityLoop.commandManager.getClientMessage() + " " + ChatFormatting.GREEN + this.getDisplayName() + " toggled on.");
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
        }
        if (this.isOn() && this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void disable() {
        if (this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
        this.enabled.setValue(false);
        if (HUD.getInstance().notifyToggles.getValue().booleanValue()) {
            TextComponentString text = new TextComponentString(InfinityLoop.commandManager.getClientMessage() + " " + ChatFormatting.RED + this.getDisplayName() + " toggled off.");
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
        }
        this.setToggled(false);
        this.onDisable();
    }

    public void toggle() {
        if (this.toggled) {
            this.disable();
        } else {
            this.enable();
        }
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public void setDisplayName(String name) {
        Module module = InfinityLoop.moduleManager.getModuleByDisplayName(name);
        Module originalModule = InfinityLoop.moduleManager.getModuleByName(name);
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

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public void setUndrawn() {
        this.drawn.setValue(null);
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
        return this.getDisplayName() + ChatFormatting.GRAY + (this.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
    }

}

