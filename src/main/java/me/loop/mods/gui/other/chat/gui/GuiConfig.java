package me.loop.mods.gui.other.chat.gui;

import me.loop.mods.gui.other.chat.BetterChat;
import me.loop.mods.gui.other.chat.ChatSettings;
import me.loop.mods.gui.other.chat.handlers.InjectUtile;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiConfig extends GuiScreen
{
    private final ChatSettings settings;
    private final List<ITextComponent> exampleChat;
    private boolean dragging;
    private int chatLeft;
    private int chatRight;
    private int chatTop;
    private int chatBottom;
    private int dragStartX;
    private int dragStartY;
    private GuiButton clearButton;
    private GuiButton smoothButton;
    private GuiSlider scaleSlider;
    private GuiSlider widthSlider;
    
    public GuiConfig() {
        this.exampleChat = new ArrayList<ITextComponent>();
        this.settings = BetterChat.getSettings();
        this.exampleChat.add((ITextComponent)new TextComponentString(I18n.format("gui.betterchat.text.example3",  new Object[0])));
        this.exampleChat.add((ITextComponent)new TextComponentString(I18n.format("gui.betterchat.text.example2",  new Object[0])));
        this.exampleChat.add((ITextComponent)new TextComponentString(I18n.format("gui.betterchat.text.example1",  new Object[0])));
    }
    
    public void initGui() {
        InjectUtile.chatGUI.configuring = true;
        this.buttonList.add(this.clearButton = new GuiButton(0,  this.width / 2 - 120,  this.height / 2 - 50,  240,  20,  this.getPropName("clear") + " " + this.getColoredBool("clear",  this.settings.clear)));
        this.buttonList.add(this.smoothButton = new GuiButton(1,  this.width / 2 - 120,  this.height / 2 - 25,  240,  20,  this.getPropName("smooth") + " " + this.getColoredBool("smooth",  this.settings.smooth)));
        this.buttonList.add(this.scaleSlider = new GuiSlider(3,  this.width / 2 - 120,  this.height / 2,  240,  20,  this.getPropName("scale") + " ",  "%",  0.0,  100.0,  (double)(this.mc.gameSettings.chatScale * 100.0f),  false,  true));
        this.buttonList.add(this.widthSlider = new GuiSlider(4,  this.width / 2 - 120,  this.height / 2 + 25,  240,  20,  this.getPropName("width") + " ",  "px",  40.0,  320.0,  (double)GuiNewChat.calculateChatboxWidth(this.mc.gameSettings.chatWidth),  false,  true));
        this.buttonList.add(new GuiButton(2,  this.width / 2 - 120,  this.height / 2 + 50,  240,  20,  this.getPropName("reset")));
    }
    
    public void drawScreen(final int mouseX,  final int mouseY,  final float partialTicks) {
        super.drawScreen(mouseX,  mouseY,  partialTicks);
        this.drawCenteredString(this.mc.fontRenderer,  I18n.format("gui.betterchat.text.title",  new Object[] { TextFormatting.GREEN + TextFormatting.BOLD.toString() + "Better Chat" + TextFormatting.RESET,  TextFormatting.AQUA + TextFormatting.BOLD.toString() + "LlamaLad7" }),  this.width / 2,  this.height / 2 - 75,  16777215);
        this.drawCenteredString(this.mc.fontRenderer,  I18n.format("gui.betterchat.text.drag",  new Object[0]),  this.width / 2,  this.height / 2 - 63,  16777215);
        if (this.dragging) {
            final ChatSettings settings = this.settings;
            settings.xOffset += mouseX - this.dragStartX;
            final ChatSettings settings2 = this.settings;
            settings2.yOffset += mouseY - this.dragStartY;
            this.dragStartX = mouseX;
            this.dragStartY = mouseY;
        }
        this.mc.gameSettings.chatScale = this.scaleSlider.getValueInt() / 100.0f;
        this.mc.gameSettings.chatWidth = (this.widthSlider.getValueInt() - 40.0f) / 280.0f;
        this.drawExampleChat();
    }
    
    public void drawExampleChat() {
        final List<ITextComponent> lines = new ArrayList<ITextComponent>();
        final int i = MathHelper.floor(InjectUtile.chatGUI.getChatWidth() / InjectUtile.chatGUI.getChatScale());
        for (final ITextComponent line : this.exampleChat) {
            lines.addAll(GuiUtilRenderComponents.splitText(line,  i,  this.mc.fontRenderer,  false,  false));
        }
        Collections.reverse(lines);
        GlStateManager.pushMatrix();
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.translate(2.0f + this.settings.xOffset,  8.0f + this.settings.yOffset + scaledresolution.getScaledHeight() - 48.0f,  0.0f);
        final float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
        final float f2 = this.mc.gameSettings.chatScale;
        final int k = MathHelper.ceil(InjectUtile.chatGUI.getChatWidth() / f2);
        GlStateManager.scale(f2,  f2,  1.0f);
        int i2 = 0;
        final double d0 = 1.0;
        int l1 = (int)(255.0 * d0);
        l1 *= (int)f;
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        this.chatLeft = this.settings.xOffset;
        this.chatRight = (int)(this.settings.xOffset + (k + 4) * f2);
        this.chatBottom = 8 + this.settings.yOffset + scaledresolution.getScaledHeight() - 48;
        for (final ITextComponent message : lines) {
            final int j2 = -i2 * 9;
            if (!this.settings.clear) {
                drawRect(-2,  j2 - 9,  k + 4,  j2,  l1 / 2 << 24);
            }
            this.mc.fontRenderer.drawStringWithShadow(message.getFormattedText(),  0.0f,  (float)(j2 - 8),  16777215 + (l1 << 24));
            ++i2;
        }
        this.chatTop = (int)(8 + this.settings.yOffset + scaledresolution.getScaledHeight() - 48 + -i2 * 9 * f2);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public void mouseClicked(final int mouseX,  final int mouseY,  final int mouseButton) throws IOException {
        super.mouseClicked(mouseX,  mouseY,  mouseButton);
        if (mouseButton == 0 && mouseX >= this.chatLeft && mouseX <= this.chatRight && mouseY >= this.chatTop && mouseY <= this.chatBottom) {
            this.dragging = true;
            this.dragStartX = mouseX;
            this.dragStartY = mouseY;
        }
    }
    
    public void mouseReleased(final int mouseX,  final int mouseY,  final int mouseButton) {
        super.mouseReleased(mouseX,  mouseY,  mouseButton);
        this.dragging = false;
    }
    
    public void onGuiClosed() {
        this.settings.saveConfig();
        InjectUtile.chatGUI.configuring = false;
        this.mc.gameSettings.saveOptions();
    }
    
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                this.settings.clear = !this.settings.clear;
                button.displayString = this.getPropName("clear") + " " + this.getColoredBool("clear",  this.settings.clear);
                break;
            }
            case 1: {
                this.settings.smooth = !this.settings.smooth;
                button.displayString = this.getPropName("smooth") + " " + this.getColoredBool("smooth",  this.settings.smooth);
                break;
            }
            case 2: {
                this.settings.resetConfig();
                this.clearButton.displayString = this.getPropName("clear") + " " + this.getColoredBool("clear",  this.settings.clear);
                this.smoothButton.displayString = this.getPropName("smooth") + " " + this.getColoredBool("smooth",  this.settings.smooth);
                this.scaleSlider.setValue((double)(this.mc.gameSettings.chatScale * 100.0f));
                this.scaleSlider.updateSlider();
                this.widthSlider.setValue((double)GuiNewChat.calculateChatboxWidth(this.mc.gameSettings.chatWidth));
                this.widthSlider.updateSlider();
                break;
            }
        }
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    private String getColoredBool(final String prop,  final boolean bool) {
        if (bool) {
            return TextFormatting.GREEN + I18n.format("gui.betterchat.text." + prop + ".enabled",  new Object[0]);
        }
        return TextFormatting.RED + I18n.format("gui.betterchat.text." + prop + ".disabled",  new Object[0]);
    }
    
    private String getPropName(final String prop) {
        return I18n.format("gui.betterchat.text." + prop + ".name",  new Object[0]);
    }
}
