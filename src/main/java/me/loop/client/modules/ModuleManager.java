package me.loop.client.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.api.events.impl.render.Render2DEvent;
import me.loop.api.events.impl.render.Render3DEvent;
import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.Util;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModuleManager
        extends Client {
    public static ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();
    public Animation animationThread;

    public void init() {
        // Test
        modules.add(new Test());

        // Client
        modules.add(new CSGui());
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
        modules.add(new AutoGG());
        modules.add(new BlockTweaks());
        modules.add(new ChatModifier());
        modules.add(new ExtraTab());
        modules.add(new MCF());
        modules.add(new NoHandShake());
        modules.add(new PopCounter());
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
        //modules.add(new ShaderESP());
        modules.add(new MotionBlur());
        modules.add(new PlayerTrails());
        modules.add(new DeathEffects());
        //modules.add(new NoCluster());
        modules.add(new ShadowESP());
        modules.add(new me.loop.client.modules.impl.render.Animation());
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
        modules.add(new Trajectories());;
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
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

    public boolean isModuleEnabled(final String name) {
        final Module module = this.modules.stream().filter(ModuleManager::isModuleEnabled).findFirst().orElse(null);
        return module != null && module.isOn();
    }

    private static boolean isModuleEnabled(Module module) {
        return false;
    }

    public static boolean isModuleEnabled(final String name, final Module m) {
        return m.getName().equals(name);
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> modules = new ArrayList<>();

        for (Module module : this.modules) {
            if (!module.isOn()) continue;
            modules.add(module);
        }

        return modules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : modules) {
            if (!module.isOn() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
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
        modules.stream().filter(Module::isListening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        modules.stream().filter(Module::isOn).forEach(Module::onUpdate);
    }

    public void onTick() {
        modules.stream().filter(Module::isOn).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        modules.stream().filter(Module::isOn).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        modules.stream().filter(Module::isOn).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
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

    private class Animation
            extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (Module module : ModuleManager.this.sortedModules) {
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isOn() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isOff() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            } else {
                for (String e : ModuleManager.this.sortedModulesABC) {
                    Module module = Managers.moduleManager.getModuleByName(e);
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isOn() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isOff() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}

