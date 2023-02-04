package com.me.infinity.loop.features.gui;

import com.me.infinity.loop.Loop;
import com.me.infinity.loop.features.Feature;
import com.me.infinity.loop.features.gui.components.Component;
import com.me.infinity.loop.features.gui.components.items.Item;
import com.me.infinity.loop.features.gui.components.items.buttons.ModuleButton;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.client.ClickGui;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.util.renders.ColorUtil;
import com.me.infinity.loop.util.renders.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class InfinityLoopGui
        extends GuiScreen {
    private static InfinityLoopGui INSTANCE;
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
        int x = -85;
        for (final Module.Category category : Loop.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 92, 27, true) {

                @Override
                public void setupItems() {
                    Loop.moduleManager.getModulesByCategory(category).forEach(module -> {
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
        for (final com.me.infinity.loop.features.gui.components.Component component : this.components) {
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
        if (ClickGui.getInstance().background.getValue()) {
            if (ClickGui.getInstance().dark.getValue()) {
                this.drawDefaultBackground();
            }
            if (ClickGui.getInstance().gradiant.getValue()) {
                this.drawGradientRect(0, 0, sr.getScaledWidth(),sr.getScaledHeight() + ClickGui.getInstance().gradiantHeight.getValue(), 0, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().hoverAlpha.getValue() / 2).getRGB());
                if (ClickGui.getInstance().gradiant.getValue() && ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                    this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() + ClickGui.getInstance().gradiantHeight.getValue().intValue(), 0, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);
                }
            }
            if (ClickGui.getInstance().blur.getValue()) {
                RenderUtil.drawBlurryRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), ClickGui.getInstance().blurAmount.getValue(), ClickGui.getInstance().blurSize.getValue());
            }
        }
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
        if (ClickGui.getInstance().colorSync.getValue()) {
            RenderUtil.drawRect(0, 0, sr.getScaledWidth(), 21, new Color(Colors.getInstance().red.getValue(), Colors.getInstance().green.getValue(), Colors.getInstance().blue.getValue(), 250).getRGB());
        } else {
            RenderUtil.drawRect(0, 0, sr.getScaledWidth(), 21, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 250).getRGB());
        }
        if (ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
            RenderUtil.drawRect(0, 0, sr.getScaledWidth(), 21,(Colors.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color);
        }
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), 20, new Color(15,15,15,255).getRGB());
        String str = ChatFormatting.WHITE + (new SimpleDateFormat("h:mm a")).format(new Date());
        String strT = ChatFormatting.WHITE + (new SimpleDateFormat("d/m/y")).format(new Date());
        Loop.textManager.drawStringWithShadow(str , 5, 1, 0);
        Loop.textManager.drawStringWithShadow(strT, 5, 12, 0);
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

    public final ArrayList<com.me.infinity.loop.features.gui.components.Component> getComponents() {
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

    public com.me.infinity.loop.features.gui.components.Component getComponentByName(String name) {
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

