package me.loop.client.gui.components.items.buttons;

import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.api.utils.impl.renders.helper.RoundedShader;
import me.loop.client.commands.Command;
import me.loop.client.gui.components.items.Item;
import me.loop.client.gui.font.FontRender;
import me.loop.client.modules.Module;
import me.loop.client.modules.impl.client.ClickGui.ClickGui;
import me.loop.client.modules.impl.client.ClickGui.ClickguiEnum;
import me.loop.client.modules.impl.client.Colors;
import me.loop.client.modules.settings.Setting;
import me.loop.client.modules.settings.impl.Bind;
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
                if (setting.getValue() instanceof ColorButton) {
                    newItems.add(new ColorButton(setting));
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

            ClickGui gui = Managers.moduleManager.getModuleByClass(ClickGui.class);
            if (ClickGui.getInstance().butonIcon.getValue() == ClickguiEnum.Icon.OpenColse) {
                if (module.getSettings().size() > 4)
                    if (ClickGui.getInstance().butonIcon.getValue() == ClickguiEnum.Icon.OpenColse)
                        FontRender.drawCentString6(module.isOn() ? gui.close.getValue() : gui.open.getValue(), (float) x + (float) width - 8f, (float) y + 6, -1);
            } else if (ClickGui.getInstance().butonIcon.getValue() == ClickguiEnum.Icon.ShowBind) {
                if (!module.getBind().toString().equalsIgnoreCase("none"))
                    FontRender.drawString5(module.getBind().toString(), (float) x + (float) width - FontRender.getStringWidth5(module.getBind().toString()) - 3f, (float) y + 6, -1);
            }

            if (this.isHovering(mouseX, mouseY)) {
                if (ClickGui.getInstance().description.getValue() == ClickguiEnum.Mode.Frame) {
                    RoundedShader.drawGradientRound(15.0f, 35.0f, 10 + this.renderer.getStringWidth(this.module.getDescription()), (float) (10), 3f, outline, outline, outline, outline);
                    if (ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                        RoundedShader.drawRoundOutline(15.0f, 35.0f, 10 + this.renderer.getStringWidth(this.module.getDescription()), (float) (10), 2.8f, 0.1f, rainbow, rainbow);
                    } else {
                        RoundedShader.drawRoundOutline(15.0f, 35.0f, 10 + this.renderer.getStringWidth(this.module.getDescription()), (float) (10), 2.8f, 0.1f, fillcolor, fillcolor);
                    }
                    Managers.textManager.drawStringWithShadow(this.module.getDescription(), 17.0f, 36.0f, -1);
                } else if (ClickGui.getInstance().description.getValue() == ClickguiEnum.Mode.Folow) {
                    RenderUtil.drawRect((float) (mouseX + 10), (float) mouseY, (float) (mouseX + 10 + this.renderer.getStringWidth(this.module.getDescription())), (float) (mouseY + 10), new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), (ClickGui.getInstance().hoverAlpha.getValue() / 2)).getRGB());
                    RenderUtil.drawBorder((float) (mouseX + 10), (float) mouseY, (float) this.renderer.getStringWidth(this.module.getDescription()), 10.0f, new Color(0xCD000000));
                    this.renderer.drawStringWithShadow(this.module.getDescription(), (float) (mouseX + 10), (float) mouseY, -1);
                }
            }

            if (subOpen) {
                float height = 1.0f;
                for (Item item : items) {
                    if (!item.isHidden()) {
                        if (item instanceof ColorButton) {
                            item.setLocation(x + 1.0f, y + (height + 15.0f));
                        } else {
                            item.setLocation(x + 1.0f, y + (height += 15.0f));
                        }
                        item.setHeight((int) 15.0f);
                        item.setWidth(width - 9);
                        item.drawScreen(mouseX, mouseY, partialTicks);
                    }
                    item.update();
                }
            }
            RenderUtil.drawOutlineRect(this.x, this.y, this.x + (float) this.width, this.y + (float) this.height - 1.5f, new Color(0x73212121, true), 0.4f);
        }
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }else if(mouseButton == 2) {
                if(module.isDrawn()) {
                    module.setUndrawn();
                    Command.sendMessage(module.getName() + " is no longer Drawn.");
                }else {
                    module.setDrawn(true);
                    Command.sendMessage(module.getName() + " is now Drawn.");
                }
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
        return this.module.isOn();
    }
}

