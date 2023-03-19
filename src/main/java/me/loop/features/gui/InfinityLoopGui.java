package me.loop.features.gui;

import me.loop.InfinityLoop;
import me.loop.features.Feature;
import me.loop.features.gui.click.Component;
import me.loop.features.gui.click.items.Item;
import me.loop.features.gui.click.items.buttons.Description;
import me.loop.features.gui.click.items.buttons.ModuleButton;
import me.loop.features.gui.screen.anchor.AnchorPoint;
import me.loop.features.gui.screen.effects.particles.ParticleSystem;
import me.loop.features.gui.screen.effects.particles.ParticlesComponent;
import me.loop.features.gui.screen.taskbar.TaskbarStage;
import me.loop.features.modules.Module;
import me.loop.features.modules.client.Colors;
import me.loop.features.modules.client.clickgui.ClickGui;
import me.loop.util.impl.ColorUtil;
import me.loop.util.impl.RenderUtil;
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
    private static InfinityLoopGui INSTANCE;
    private final ArrayList<Component> components = new ArrayList();
    private final TaskbarStage taskbarStage = new TaskbarStage();
    private ParticlesComponent particlesComponent;
    public ParticleSystem particleSystem;
    private static Description description;
    private int color;

    static {
        INSTANCE = new InfinityLoopGui();
    }

    public InfinityLoopGui() {
        this.setInstance();
        this.load();
    }

    public static InfinityLoopGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InfinityLoopGui();
        }
        return INSTANCE;
    }

    public static InfinityLoopGui getClickGui() {
        return InfinityLoopGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
        int x = -80;
        for (final Module.Category category : InfinityLoop.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 100, 50, true) {

                @Override
                public void setupItems() {
                    counter1 = new int[]{1};
                    InfinityLoop.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton(module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    public void updateModule(Module module) {
        for (Component component : this.components) {
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
        for (AnchorPoint anchorPoint : ClickGui.getInstance().getAnchorPoints()) {
            anchorPoint.updatePosition(sr);
        }
    }

    public Description getDescription() {
        return description;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        ScaledResolution sr = new ScaledResolution(mc);
        description.setDraw(false);
        if (ClickGui.getInstance().isEnabled()) {
            if (ClickGui.getInstance().dirt.getValue()) {
                this.drawBackground(1);
            }
            if (ClickGui.getInstance().dark.getValue()) {
                this.drawDefaultBackground();
            }
            if (ClickGui.getInstance().blur.getValue()) {
                RenderUtil.drawBlurryRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), ClickGui.getInstance().blurAmount.getValue(), ClickGui.getInstance().blurSize.getValue());
            }
            if (ClickGui.getInstance().gradiant.getValue()) {
                RenderUtil.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() + ClickGui.getInstance().gradiantHeight.getValue(), 0, new Color(ClickGui.getInstance().moduleEnableC.getValue().getRed(), ClickGui.getInstance().moduleEnableC.getValue().getGreen(), ClickGui.getInstance().moduleEnableC.getValue().getBlue(), ClickGui.getInstance().gradiantAlpha.getValue()).getRGB());
                if (ClickGui.getInstance().gradiant.getValue() && ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                    RenderUtil.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() + ClickGui.getInstance().gradiantHeight.getValue().intValue(), 0, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);
                }
            }
            if (this.particleSystem != null && ClickGui.getInstance().particles.getValue()) {
                this.particleSystem.render(mouseX, mouseY);
            } else {
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
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            if (ClickGui.getInstance().scroll.getValue().booleanValue()) {
                this.components.forEach(component -> component.setY(component.getY() - ClickGui.getInstance().scrollval.getValue()));
            }
        } else if (dWheel > 0 && ClickGui.getInstance().scroll.getValue().booleanValue()) {
            this.components.forEach(component -> component.setY(component.getY() + ClickGui.getInstance().scrollval.getValue()));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
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

