package com.me.infinity.loop.features.clickGui;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.Feature;
import com.me.infinity.loop.features.clickGui.screen.components.Component;
import com.me.infinity.loop.features.clickGui.screen.components.items.Item;
import com.me.infinity.loop.features.clickGui.screen.components.items.buttons.ModuleButton;
import com.me.infinity.loop.features.clickGui.screen.taskbar.Taskbar;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.client.ClickGui;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.util.renders.ColorUtil;
import com.me.infinity.loop.util.renders.RenderUtil;
import com.me.infinity.loop.util.renders.helper.MousePosition;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class InfinityLoopGui
        extends GuiScreen {
    private static InfinityLoopGui INSTANCE;
    private final Taskbar taskbar = new Taskbar();
    private final MousePosition mouse = new MousePosition(Vec2f.ZERO, false, false, false, false);
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
        for (final Module.Category category : InfinityLoop.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 100, 40, true) {

                @Override
                public void setupItems() {
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
        for (final com.me.infinity.loop.features.clickGui.screen.components.Component component : this.components) {
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

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        ScaledResolution resolution = new ScaledResolution(mc);
        if (ClickGui.getInstance().background.getValue()) {
            if (ClickGui.getInstance().gradiant.getValue()) {
                RenderUtil.drawGradientRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight() + ClickGui.getInstance().gradiantHeight.getValue(), 0, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().hoverAlpha.getValue() / 2).getRGB());
                if (ClickGui.getInstance().gradiant.getValue() && ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                    RenderUtil.drawGradientRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight() + ClickGui.getInstance().gradiantHeight.getValue().intValue(), 0, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);
                }
            }
            if (ClickGui.getInstance().blur.getValue()) {
                RenderUtil.drawBlurryRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), ClickGui.getInstance().blurAmount.getValue(), ClickGui.getInstance().blurSize.getValue());
            }
            if (ClickGui.getInstance().dark.getValue()) {
                this.drawDefaultBackground();
            }

        }
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
        taskbar.drawComponent();
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

    public final ArrayList<com.me.infinity.loop.features.clickGui.screen.components.Component> getComponents() {
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

