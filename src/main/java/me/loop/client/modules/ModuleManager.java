package me.loop.client.modules;

import me.loop.api.events.impl.render.Render2DEvent;
import me.loop.api.events.impl.render.Render3DEvent;
import me.loop.client.Client;
import me.loop.client.gui.InfinityLoopGui;
import me.loop.client.modules.impl.client.*;
import me.loop.client.modules.impl.client.ClickGui.ClickGui;
import me.loop.client.modules.impl.combat.*;
import me.loop.client.modules.impl.misc.*;
import me.loop.client.modules.impl.movement.*;
import me.loop.client.modules.impl.player.*;
import me.loop.client.modules.impl.render.*;
import me.loop.client.modules.impl.render.deatheffects.DeathEffects;
import me.loop.client.modules.impl.render.motionblur.MotionBlur;
import me.loop.client.modules.impl.test.Test;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager
        extends Client {
    public static ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<Module> alphabeticallySortedModules = new ArrayList<Module>();

    public void init() {
        // Test
        modules.add(new Test());

        // Client
        modules.add(new DisplayNotify());
        modules.add(new DisplayNotify());
        modules.add(new BlurExtends());
        modules.add(new ClickGui());
        modules.add(new RPC());
        modules.add(new GameChanger());
        modules.add(new Colors());
        modules.add(new FontMod());
        modules.add(new HUD());
        modules.add(new MainSettings());
        modules.add(new Windows());

        // Combat
        modules.add(new AutoArmor());
        modules.add(new AutoCrystal());
        modules.add(new Criticals());
        modules.add(new Killaura());
        modules.add(new Offhand());

        // Misc
        modules.add(new Notifications());
        modules.add(new AutoGG());
        modules.add(new BlockTweaks());
        modules.add(new ChatModifier());
        modules.add(new MCF());
        modules.add(new NoHandShake());
        modules.add(new ToolTips());

        // Movement
        modules.add(new NoSlow());
        modules.add(new NoVoid());
        modules.add(new ReverseStep());
        modules.add(new Step());
        modules.add(new TimerSpeed());

        // Player
        modules.add(new AutoDoSmth());
        modules.add(new FakePlayer());
        modules.add(new FastPlace());
        modules.add(new MCP());
        modules.add(new MultiTask());
        modules.add(new Replenish());
        modules.add(new Search());
        modules.add(new TpsSync());

        // Render
        modules.add(new CustomTime());
        //modules.add(new ShaderESP());
        modules.add(new MotionBlur());
        modules.add(new PlayerTrails());
        modules.add(new DeathEffects());
        //modules.add(new NoCluster());
        modules.add(new ShadowESP());
        modules.add(new CustomAnimation());
        modules.add(new ArrowESP());
        modules.add(new BlockHighlight());
        modules.add(new BreadCrumbs());
        modules.add(new BurrowESP());
        modules.add(new CameraClip());
        //modules.add(new Dismemberment());
        modules.add(new ESP());
        modules.add(new FogColor());
        modules.add(new HandChams());
        modules.add(new HoleESP());
        modules.add(new ItemPhysics());
        modules.add(new PearlRender());
        modules.add(new Skeleton());
        modules.add(new SmallShield());
        modules.add(new Trajectories());

        for (Module module : this.modules) {
            module.animation.start();
        }
    }
    public <T extends Module> T getModuleT(Class<T> clazz) {
        return (T)((Module)this.modules.stream().filter(module -> module.getClass() == clazz).map(module -> module).findFirst().orElse(null));
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public void enableModule(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        if (module != null) {
            ((Module) module).enable();
        }
    }

    public void disableModule(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        if (module != null) {
            ((Module) module).disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        return module != null && ((Module) module).isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.isEnabled() && !module.isSliding()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Category> getCategories() {
        return Arrays.asList(Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Client::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Client::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Client::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Client::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void alphabeticallySortModules() {
        this.alphabeticallySortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(Module::getDisplayName)).collect(Collectors.toList());
    }

    public void onLogout() {
        modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof InfinityLoopGui) {
            return;
        }
        modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    public List<Module> getAnimationModules(Category category) {
        ArrayList<Module> animationModules = new ArrayList<Module>();
        for (Module module : this.getEnabledModules()) {
            if (module.getCategory() != category || module.isDisabled() || !module.isSliding() || !module.isDrawn())
                continue;
            animationModules.add(module);
        }
        return animationModules;
    }
}

