package infinityloop.features.gui;

import infinityloop.InfinityLoop;
import infinityloop.features.Feature;
import infinityloop.features.gui.screen.anchor.AnchorPoint;
import infinityloop.features.gui.components.Component;
import infinityloop.features.gui.components.items.Item;
import infinityloop.features.gui.components.items.buttons.ModuleButton;
import infinityloop.features.gui.screen.particles.ParticleSystem;
import infinityloop.features.gui.screen.particles.ParticlesComponent;
import infinityloop.features.gui.screen.taskbar.TaskbarStage;
import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.modules.client.ClickGui.ClickGui;
import infinityloop.features.modules.client.Colors;
import infinityloop.util.utils.renders.ColorUtil;
import infinityloop.util.utils.renders.RenderUtil;
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
    private final TaskbarStage taskbarStage = new TaskbarStage();

    public ParticleSystem particleSystem;
    private ParticlesComponent particlesComponent;
    private int color;

    static {
        INSTANCE = new InfinityLoopGui();
    }

    private final ArrayList<Component> components = new ArrayList();

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
        for (final ModuleCategory category : InfinityLoop.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 96, 50, true) {

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

    public void updateModule(final Module module) {
        for (final infinityloop.features.gui.components.Component component : this.components) {
            for (final Item item : component.getItems()) {
                if (item instanceof ModuleButton) {
                    final ModuleButton button = (ModuleButton) item;
                    final Module mod = button.getModule();
                    if (module != null && module.equals(mod)) {
                        button.initSettings();
                        break;
                    }
                }
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

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        ScaledResolution rs = new ScaledResolution(mc);
        if (ClickGui.getInstance().background.getValue()) {
            if (ClickGui.getInstance().dark.getValue()) {
                this.drawDefaultBackground();
            }
            if (ClickGui.getInstance().blur.getValue()) {
                RenderUtil.drawBlurryRect(0, 0, rs.getScaledWidth(), rs.getScaledHeight(), ClickGui.getInstance().blurAmount.getValue(), ClickGui.getInstance().blurSize.getValue());
            }
            if (ClickGui.getInstance().gradiant.getValue()) {
                RenderUtil.drawGradientRect(0, 0, rs.getScaledWidth(), rs.getScaledHeight() + ClickGui.getInstance().gradiantHeight.getValue(), 0, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().gradiantAlpha.getValue()).getRGB());
                if (ClickGui.getInstance().gradiant.getValue() && ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                    RenderUtil.drawGradientRect(0, 0, rs.getScaledWidth(), rs.getScaledHeight() + ClickGui.getInstance().gradiantHeight.getValue().intValue(), 0, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);
                }
            }
            if (this.particleSystem != null && ClickGui.getInstance().particles.getValue()) {
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
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
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
        InfinityLoopGui.INSTANCE = new InfinityLoopGui();
    }
}

