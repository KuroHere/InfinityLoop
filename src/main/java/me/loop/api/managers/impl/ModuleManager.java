package me.loop.api.managers.impl;

import me.loop.api.events.impl.render.Render2DEvent;
import me.loop.api.events.impl.render.Render3DEvent;
import me.loop.mods.Mod;
import me.loop.mods.gui.InfinityLoopGui;
import me.loop.mods.modules.Category;
import me.loop.mods.modules.Module;
import me.loop.mods.modules.impl.client.*;
import me.loop.mods.modules.impl.client.ClickGui.ClickGui;
import me.loop.mods.modules.impl.combat.*;
import me.loop.mods.modules.impl.misc.*;
import me.loop.mods.modules.impl.movement.*;
import me.loop.mods.modules.impl.player.*;
import me.loop.mods.modules.impl.render.*;
import me.loop.mods.modules.impl.render.motionblur.MotionBlur;
import me.loop.mods.modules.impl.test.Test;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager extends Mod {

    public static ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<Module> alphabeticallySortedModules = new ArrayList<Module>();

    public void init() {
        registerModules();
    }

    private void registerModules() {
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
        modules.add(new Dismemberment());
        modules.add(new HitMarkers());
        modules.add(new CustomTime());
        modules.add(new MotionBlur());
        modules.add(new DeathEffects());
        modules.add(new ShadowESP());
        modules.add(new CustomAnimation());
        modules.add(new ArrowESP());
        modules.add(new BlockHighlight());
        modules.add(new BreadCrumbs());
        modules.add(new BurrowESP());
        modules.add(new CameraClip());
        modules.add(new ESP());
        modules.add(new FogColor());
        modules.add(new HandChams());
        modules.add(new HoleESP());
        modules.add(new ItemPhysics());
        modules.add(new PearlRender());
        modules.add(new SmallShield());
        modules.add(new Trajectories());

    }

    //ArrayList

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void alphabeticallySortModules() {
        this.alphabeticallySortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(Module::getDisplayName)).collect(Collectors.toList());
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

    //Getters

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> modules = new ArrayList<>();

        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            modules.add(module);
        }

        return modules;
    }

    public ArrayList<String> getEnabledModulesString() {
        ArrayList<String> modules = new ArrayList<>();

        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            modules.add(module.getArrayListInfo());
        }

        return modules;
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;

            return module;
        }

        return null;
    }

    public ArrayList<Module> getModulesByCategory(Category category) {
        ArrayList<Module> modules = new ArrayList<>();

        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modules.add(module);
            }
        });

        return modules;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public List<Category> getCategories() {
        return Arrays.asList(Category.values());
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    //Listeners

    public void onUnloadPre() {
        this.modules.sort(Comparator.comparing(Module::getName));
        modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyInput(int key) {
        if (key == 0 || !Keyboard.getEventKeyState() || mc.currentScreen instanceof InfinityLoopGui) return;

        modules.forEach(module -> {
            if (module.getBind().getKey() == key) {
                module.toggle();
            }
        });
    }

    //Registering override-able stuff from Module.java

    public void onLoad() {
        modules.stream().filter(Module::isListening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        modules.stream().filter(Mod::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        modules.stream().filter(Mod::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        modules.stream().filter(Mod::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        modules.stream().filter(Mod::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void onTotemPop(EntityPlayer player) {
        modules.stream().filter(Mod::isEnabled).forEach(module -> module.onTotemPop(player));
    }

    public void onDeath(EntityPlayer player) {
        modules.stream().filter(Mod::isEnabled).forEach(module -> module.onDeath(player));
    }

    public void onLogout() {
        modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        modules.forEach(Module::onLogin);
    }

}

