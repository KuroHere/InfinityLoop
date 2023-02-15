package com.me.infinity.loop.features.csgui;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.Feature;
import com.me.infinity.loop.features.ui.screen.components.Component;
import com.me.infinity.loop.features.ui.screen.components.items.Item;
import com.me.infinity.loop.features.ui.screen.components.items.buttons.ModuleButton;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.client.CSGui;
import com.me.infinity.loop.util.renders.ColorUtil;
import com.me.infinity.loop.util.renders.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class CSClickGui
        extends GuiScreen {
    private static CSClickGui INSTANCE;
    private int color;
    public boolean drag;
    private int x;
    private int y;
    private int x2;
    private int y2;

    static {
        INSTANCE = new CSClickGui();
    }

    private final ArrayList<Component> components = new ArrayList();

    public CSClickGui() {
        this.setInstance();
        this.load();
    }

    public static CSClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CSClickGui();
        }
        return INSTANCE;
    }

    public static CSClickGui getCSGui() {
        return CSClickGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
        int x = -85;
        for (final Module.Category category : InfinityLoop.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 92, 27, true) {

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
        for (final Component component : this.components) {
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
        final ScaledResolution sr = new ScaledResolution(this.mc);
        if (CSGui.getInstance().background.getValue()) {
            if (CSGui.getInstance().dark.getValue()) {
                this.drawDefaultBackground();
            }
            if (CSGui.getInstance().gradiant.getValue()) {
                this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() + CSGui.getInstance().gradiantHeight.getValue(), 0, new Color(CSGui.getInstance().red.getValue(), CSGui.getInstance().green.getValue(), CSGui.getInstance().blue.getValue(), CSGui.getInstance().hoverAlpha.getValue() / 2).getRGB());
                if (CSGui.getInstance().gradiant.getValue() || CSGui.getInstance().rainbow.getValue()) {
                    this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() + CSGui.getInstance().gradiantHeight.getValue().intValue(), 0, (CSGui.getInstance()).rainbow.getValue().booleanValue() ? (((CSGui.getInstance()).rainbowModeA.getValue() == CSGui.rainbowModeArray.Up) ? ColorUtil.rainbow((CSGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((CSGui.getInstance()).rainbowHue.getValue().intValue(), CSGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);
                }
            }
            if (CSGui.getInstance().blur.getValue()) {
                RenderUtil.drawBlurryRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), CSGui.getInstance().blurAmount.getValue(), CSGui.getInstance().blurSize.getValue());
            }
        }
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
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
        CSClickGui.INSTANCE = new CSClickGui();
    }
}


