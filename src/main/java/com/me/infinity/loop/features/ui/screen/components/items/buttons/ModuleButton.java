package com.me.infinity.loop.features.gui.screen.components.items.buttons;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.gui.font.FontRender;
import com.me.infinity.loop.features.gui.screen.components.Component;
import com.me.infinity.loop.features.gui.screen.components.items.Item;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.client.ClickGui;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.features.setting.Bind;
import com.me.infinity.loop.features.setting.ColorSetting;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.renders.ColorUtil;
import com.me.infinity.loop.util.renders.RenderUtil;
import com.me.infinity.loop.util.renders.helper.RoundedShader;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ModuleButton
        extends Button {
    private final Module module;
    private List<Item> items = new ArrayList<Item>();
    private boolean subOpen;
    private int color;

    public ModuleButton(Module module) {
        super(module.getName());
        this.module = module;
        this.initSettings();
    }

    public void initSettings() {
        ArrayList<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            newItems.add(new BindButton(this.module.getSettingByName("Keybind")));
            this.items = newItems;
            for (Setting setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add(new BooleanButton(setting));
                }
                if (setting.getValue() instanceof ColorSetting) {
                    newItems.add(new ColorPicker(setting));
                }
                if (setting.getValue() instanceof Bind && !setting.getName().equalsIgnoreCase("Keybind") && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add(new BindButton(setting));
                }
                if ((setting.getValue() instanceof String || setting.getValue() instanceof Character) && !setting.getName().equalsIgnoreCase("displayName")) {
                    newItems.add(new StringButton(setting));
                }
                if (setting.isNumberSetting() && setting.hasRestriction()) {
                    newItems.add(new Slider(setting));
                    continue;
                }
                if (!setting.isEnumSetting()) continue;
                newItems.add(new EnumButton(setting));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!this.items.isEmpty()) {
            Color outline = new Color(0xCD000000, true);
            Color fillcolor = new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().hoverAlpha.getValue());
            Color rainbow = new Color(Colors.getInstance().rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);

            ClickGui gui = InfinityLoop.moduleManager.getModuleByClass(ClickGui.class);
            if(!ClickGui.getInstance().showBind.getValue() ){
                if (module.getSettings().size() > 4)
                    if (ClickGui.getInstance().openCloseChange.getValue())
                    FontRender.drawCentString6(isEnabled() ? gui.close.getValue() : gui.open.getValue(), (float) x + (float) width - 8f, (float) y + 6, -1);
            } else {
                if(!module.getBind().toString().equalsIgnoreCase("none"))
                    FontRender.drawString5(module.getBind().toString(), (float) x + (float) width - FontRender.getStringWidth5("[" + module.getBind().toString() + "]" ) - 3f, (float) y + 6, -1);
            }
            if (this.isHovering(mouseX, mouseY)) {
                if (ClickGui.getInstance().description.getValue() == ClickGui.Mode.Frame) {
                    RoundedShader.drawGradientRound(15.0f, 25.0f, 10 + this.renderer.getStringWidth(this.module.getDescription()), (float) (10), 3f, outline, outline, outline, outline);
                    if (ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                        RoundedShader.drawRoundOutline(15.0f, 25.0f, 10 + this.renderer.getStringWidth(this.module.getDescription()), (float) (10), 2.8f, 0.1f, rainbow, rainbow);
                    } else {
                        RoundedShader.drawRoundOutline(15.0f, 25.0f, 10 + this.renderer.getStringWidth(this.module.getDescription()), (float) (10), 2.8f, 0.1f, fillcolor, fillcolor);
                    }
                    InfinityLoop.textManager.drawStringWithShadow(this.module.getDescription(), 17.0f, 26.0f, -1);

                } else if (ClickGui.getInstance().description.getValue() == ClickGui.Mode.Folow) {
                    RenderUtil.drawRect((float) (mouseX + 10), (float) mouseY, (float) (mouseX + 10 + this.renderer.getStringWidth(this.module.getDescription())), (float) (mouseY + 10), new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB());
                    RenderUtil.drawBorder((float) (mouseX + 10), (float) mouseY, (float) this.renderer.getStringWidth(this.module.getDescription()), 10.0f, new Color(0xCD000000));
                    this.renderer.drawStringWithShadow(this.module.getDescription(), (float) (mouseX + 10), (float) mouseY, -1);
                }
            }

            if (this.subOpen) {
                float height = 1.0f;
                for (Item item : this.items) {
                    Component.counter1[0] = Component.counter1[0] + 1;
                    if (!item.isHidden()) {
                        item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                        item.setHeight(15);
                        item.setWidth(this.width - 9);
                        item.drawScreen(mouseX, mouseY, partialTicks);
                        if (item instanceof ColorPicker)
                            item.setHeight(56);
                    }
                    item.update();
                }
            }
            //Drawable.drawBlurredShadow((int) x, (int) (y + height), (int) width, 3, 9, new Color(0, 0, 0, 160));
        }
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            if (this.subOpen) {
                for (Item item : this.items) {
                    if (item.isHidden()) continue;
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        super.onKeyTyped(typedChar, keyCode);
        if (!this.items.isEmpty() && this.subOpen) {
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                item.onKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public int getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                height += item.getHeight() + 1;
            }
            return height + 2;
        }
        return 14;
    }

    public Module getModule() {
        return this.module;
    }

    @Override
    public void toggle() {
        this.module.toggle();
    }

    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}

