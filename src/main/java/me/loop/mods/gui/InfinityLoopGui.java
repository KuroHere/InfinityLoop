package me.loop.mods.gui;

import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.mods.Mod;
import me.loop.mods.gui.click.Component;
import me.loop.mods.gui.click.items.Item;
import me.loop.mods.gui.click.items.buttons.Description;
import me.loop.mods.gui.click.items.buttons.ModuleButton;
import me.loop.mods.gui.screen.anchor.AnchorPoint;
import me.loop.mods.gui.screen.particles.ParticleSystem;
import me.loop.mods.gui.screen.particles.ParticlesComponent;
import me.loop.mods.gui.screen.taskbar.TaskbarStage;
import me.loop.mods.modules.Category;
import me.loop.mods.modules.Module;
import me.loop.mods.modules.impl.client.ClickGui.ClickGui;
import me.loop.mods.modules.impl.client.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class InfinityLoopGui
        extends GuiScreen {

    public static InfinityLoopGui INSTANCE;
    private final TaskbarStage taskbarStage = new TaskbarStage();
    private static Description description;
    public ParticleSystem particleSystem;
    private ParticlesComponent particlesComponent;
    private int color;
    private final ArrayList<Component> components = new ArrayList();

    public InfinityLoopGui() {
        this.onLoad();
    }

    private void onLoad() {
        INSTANCE = this;
        int x = -80;
        for (final Category category : Managers.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 96, 50, true) {

                @Override
                public void setupItems() {
                    counter1 = new int[]{1};

                    Managers.moduleManager.getModulesByCategory(category).forEach(module -> addButton(new ModuleButton(module)));
                }
            });
        }

        components.forEach(components -> components.getItems().sort(Comparator.comparing(Mod::getName)));
    }

    public void updateModule(Module module) {
        for (Component component : components) {

            for (Item item : component.getItems()) {

                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton) item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
            }
        }
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        super.onResize(mcIn, w, h);

        if (this.particleSystem != null)
            this.particleSystem = new ParticleSystem(new ScaledResolution(mcIn));

        final ScaledResolution sr = new ScaledResolution(mcIn);
        for (AnchorPoint anchorPoint : ClickGui.INSTANCE.getAnchorPoints()) {
            anchorPoint.updatePosition(sr);
        }
    }

    public Description getDescription() {
        return description;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        ScaledResolution sr = new ScaledResolution(mc);
        if (ClickGui.INSTANCE.isOn()) {
            if(ClickGui.INSTANCE.dirt.getValue()){
                this.drawBackground(1);
            }
            if (ClickGui.INSTANCE.dark.getValue()) {
                this.drawDefaultBackground();
            }
            if (ClickGui.INSTANCE.blur.getValue()) {
                RenderUtil.drawBlurryRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), ClickGui.INSTANCE.blurAmount.getValue(), ClickGui.INSTANCE.blurSize.getValue());
            }
            if (ClickGui.INSTANCE.gradiant.getValue()) {
                RenderUtil.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() + ClickGui.INSTANCE.gradiantHeight.getValue(), 0, new Color(ClickGui.INSTANCE.moduleEnableC.getValue().getRed(), ClickGui.INSTANCE.moduleEnableC.getValue().getGreen(), ClickGui.INSTANCE.moduleEnableC.getValue().getBlue(), ClickGui.INSTANCE.gradiantAlpha.getValue()).getRGB());
                if (ClickGui.INSTANCE.gradiant.getValue() && ClickGui.INSTANCE.colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                    RenderUtil.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() + ClickGui.INSTANCE.gradiantHeight.getValue().intValue(), 0, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.INSTANCE.hoverAlpha.getValue()).getRGB()) : this.color);
                }
            }
            if (this.particleSystem != null && ClickGui.INSTANCE.particles.getValue()) {
                this.particleSystem.render(mouseX, mouseY);
            }
            else {
                this.particleSystem = new ParticleSystem(new ScaledResolution(this.mc));
            }
        }
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
        taskbarStage.drawComponent();
    }

    public void updateScreen() {
        super.updateScreen();
        if (this.particleSystem != null) {
            if (this.particlesComponent != null) {
                if (!this.particlesComponent.isVisible()) {
                    this.particleSystem = null;
                    return;
                }
            }

            this.particleSystem.update();
        } else {
            if (this.particlesComponent != null) {
                if (this.particlesComponent.isVisible()) {
                    this.particleSystem = new ParticleSystem(new ScaledResolution(mc));
                }
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return components;
    }
    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            if (ClickGui.INSTANCE.scroll.getValue().booleanValue()) {
                this.components.forEach(component -> component.setY(component.getY() - ClickGui.INSTANCE.scrollval.getValue()));
            }
        } else if (dWheel > 0 && ClickGui.INSTANCE.scroll.getValue().booleanValue()) {
            this.components.forEach(component -> component.setY(component.getY() + ClickGui.INSTANCE.scrollval.getValue()));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }

    static {
        INSTANCE = new InfinityLoopGui();
        description = new Description("", 0.0f, 0.0f);
    }
}


