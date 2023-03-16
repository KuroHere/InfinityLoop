package me.loop.mods.gui.click.items.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.InfinityLoop;
import me.loop.api.utils.impl.maths.ImageUtil;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.api.utils.impl.renders.helper.RoundedShader;
import me.loop.mods.gui.InfinityLoopGui;
import me.loop.mods.gui.click.items.Item;
import me.loop.mods.gui.font.FontRender;
import me.loop.mods.modules.Module;
import me.loop.mods.modules.impl.client.ClickGui.ClickEnum;
import me.loop.mods.modules.impl.client.ClickGui.ClickGui;
import me.loop.mods.modules.impl.client.Colors;
import me.loop.mods.settings.Bind;
import me.loop.mods.settings.Setting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ModuleButton
        extends Button {
    private final Module module;
    public int[] counter1 = new int[]{1};
    private List<Item> items = new ArrayList<Item>();
    private boolean subOpen;
    private int progress;
    private int color;

    public ModuleButton(Module module) {
        super(module.getName());
        progress = 0;
        this.module = module;
        this.initSettings();
    }

    public void initSettings() {
        ArrayList<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            newItems.add(new BindButton(this.module.getSettingByName("Keybind")));
            for (Setting setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add(new BooleanButton(setting));
                }
                if (setting.getValue() instanceof Bind && !setting.getName().equalsIgnoreCase("Keybind") && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add(new BindButton(setting));
                }
                if ((setting.getValue() instanceof String || setting.getValue() instanceof Character) && !setting.getName().equalsIgnoreCase("displayName")) {
                    newItems.add(new StringButton(setting));
                }
                if (setting.getValue() instanceof Color)
                    newItems.add(new ColorButton(setting));

                if (setting.isNumberSetting()) {
                    if (setting.hasRestriction()) {
                        newItems.add(new SliderButton(setting));
                        continue;
                    }
                    newItems.add(new UnlimitedSlider(setting));
                }
                if (!setting.isEnumSetting()) continue;
                newItems.add(new EnumButton(setting));
            }
        }
        this.items = newItems;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!this.items.isEmpty()) {

            boolean frame = ClickGui.INSTANCE.description.getValue() == ClickEnum.Mode.Frame;
            boolean follow = ClickGui.INSTANCE.description.getValue() == ClickEnum.Mode.Folow;

            Color outline = new Color(0xCD000000, true);
            Color fillcolor = new Color(ClickGui.INSTANCE.moduleMainC.getValue().getRed(), ClickGui.INSTANCE.moduleMainC.getValue().getGreen(), ClickGui.INSTANCE.moduleMainC.getValue().getBlue(), ClickGui.INSTANCE.hoverAlpha.getValue());
            Color rainbow = new Color(Colors.getInstance().rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.INSTANCE.hoverAlpha.getValue()).getRGB()) : this.color);

            RenderUtil.drawOutlineRect(this.x, this.y, this.x + (float) this.width, this.y + (float) this.height - 1.5f, new Color(0x73212121, true), 0.4f);

            drawGear();

            if (subOpen) {

                ++progress;

                float height = 1.0f;
                for (Item item : this.items) {
                    counter1[0] = counter1[0] + 1;
                    if (!item.isHidden()) {
                        item.setHeight((int) 15.0f);
                        item.setWidth(width - 9);
                        item.drawScreen(mouseX, mouseY, partialTicks);

                        if (item instanceof ColorButton && ((ColorButton)item).setting.isOpen) {
                            height += 110.0f;
                        }

                        if (item instanceof EnumButton && ((EnumButton)item).setting.isOpen) {
                            height += ((EnumButton) item).setting.getValue().getClass().getEnumConstants().length * 12;
                        }

                    }
                    item.update();
                }
            }
            if (this.isHovering(mouseX, mouseY)) {

                if (frame) {
                    RoundedShader.drawGradientRound(15.0f, 35.0f, 10 + this.renderer.getStringWidth(this.module.getDescription()), (float) (10), 3f, outline, outline, outline, outline);
                    if (ClickGui.INSTANCE.colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                        RoundedShader.drawRoundOutline(15.0f, 35.0f, 10 + this.renderer.getStringWidth(this.module.getDescription()), (float) (10), 2.8f, 0.1f, rainbow, rainbow);
                    } else {
                        RoundedShader.drawRoundOutline(15.0f, 35.0f, 10 + this.renderer.getStringWidth(this.module.getDescription()), (float) (10), 2.8f, 0.1f, fillcolor, fillcolor);
                    }
                    InfinityLoop.textManager.drawStringWithShadow(this.module.getDescription(), 17.0f, 36.0f, -1);
                }
                if (follow) {

                    Description descriptionDisplay = InfinityLoopGui.INSTANCE.getDescription();
                    descriptionDisplay.setDescription(this.module.getDescription());
                    descriptionDisplay.setLocation(mouseX + 2, mouseY + 1);
                    descriptionDisplay.setDraw(true);

                }
            }

        }
    }

    public void drawGear() {
        boolean future = ClickGui.INSTANCE.setting.getValue() == ClickEnum.Style.Future;
        boolean chart = ClickGui.INSTANCE.butonIcon.getValue() == ClickEnum.Style.OpenColse;
        boolean bind = ClickGui.INSTANCE.butonIcon.getValue() == ClickEnum.Style.ShowBind;

        if (future) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/gear.png"));
            GlStateManager.translate(getX() + getWidth() - 6.7F, getY() + 7.7F - 0.3F, 0.0F);
            GlStateManager.rotate(ImageUtil.calculateRotation((float) progress), 0.0F, 0.0F, 1.0F);
            ImageUtil.drawModalRect(-5, -5, 0.0F, 0.0F, 10, 10, 10, 10, 10.0F, 10.0F);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
        if (chart) {
            if (module.getSettings().size() > 4) {
                ClickGui gui = InfinityLoop.moduleManager.getModuleByClass(ClickGui.class);
                String color = (module.isOn() || chart) ? "" : "" + ChatFormatting.GRAY;
                String gear = subOpen ? gui.close.getValue() : gui.open.getValue();

                FontRender.drawCentString6(color + gear, (float) x + (float) width - 8f, (float) y + 6, -1);
            }
        }
        if (bind) {
            if (!module.getBind().toString().equalsIgnoreCase("none"))
                FontRender.drawString5(module.getBind().toString(), (float) x + (float) width - FontRender.getStringWidth5(module.getBind().toString()) - 3f, (float) y + 6, -1);

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

