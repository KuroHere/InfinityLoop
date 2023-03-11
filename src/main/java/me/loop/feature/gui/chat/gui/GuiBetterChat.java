
package me.loop.feature.gui.chat.gui;

import com.google.common.collect.Lists;
import me.loop.feature.gui.chat.BetterChat;
import me.loop.feature.gui.chat.utils.AnimationTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiBetterChat extends GuiNewChat
{
    private static final Logger LOGGER;
    public static float percentComplete;
    public static int newLines;
    public static long prevMillis;
    private final Minecraft mc;
    /**
     * A list of messages previously sent through the chat GUI
     */
    private final List<String> sentMessages = Lists.<String>newArrayList();
    /**
     * Chat lines to be displayed in the chat box
     */
    private final List<ChatLine> chatLines = Lists.<ChatLine>newArrayList();
    /**
     * List of the ChatLines currently drawn
     */
    private final List<ChatLine> drawnChatLines = Lists.<ChatLine>newArrayList();
    public boolean configuring;
    private int scrollPos;
    private boolean isScrolled;
    
    public GuiBetterChat(final Minecraft mcIn) {
        super(mcIn);
        this.mc = mcIn;
    }
    
    public static void updatePercentage(final long diff) {
        if (GuiBetterChat.percentComplete < 1.0f) {
            GuiBetterChat.percentComplete += 0.004f * diff;
        }
        GuiBetterChat.percentComplete = AnimationTools.clamp(GuiBetterChat.percentComplete,  0.0f,  1.0f);
    }
    
    public static int calculateChatboxWidth(final float scale) {
        return MathHelper.floor(scale * 280.0f + 40.0f);
    }
    
    public static int calculateChatboxHeight(final float scale) {
        return MathHelper.floor(scale * 160.0f + 20.0f);
    }
    
    public void drawChat(final int updateCounter) {
        if (this.configuring) {
            return;
        }
        if (GuiBetterChat.prevMillis == -1L) {
            GuiBetterChat.prevMillis = System.currentTimeMillis();
            return;
        }
        final long current = System.currentTimeMillis();
        final long diff = current - GuiBetterChat.prevMillis;
        GuiBetterChat.prevMillis = current;
        updatePercentage(diff);
        float t = GuiBetterChat.percentComplete;
        float percent = 1.0f - --t * t * t * t;
        percent = AnimationTools.clamp(percent,  0.0f,  1.0f);
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            final int i = this.getLineCount();
            final int j = this.drawnChatLines.size();
            final float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (j > 0) {
                final boolean flag = this.getChatOpen();
                final float f2 = this.getChatScale();
                final int k = MathHelper.ceil(this.getChatWidth() / f2);
                GlStateManager.pushMatrix();
                if (BetterChat.getSettings().smooth && !this.isScrolled) {
                    GlStateManager.translate(2.0f + BetterChat.getSettings().xOffset,  8.0f + BetterChat.getSettings().yOffset + (9.0f - 9.0f * percent) * f2,  0.0f);
                }
                else {
                    GlStateManager.translate(2.0f + BetterChat.getSettings().xOffset,  8.0f + BetterChat.getSettings().yOffset,  0.0f);
                }
                GlStateManager.scale(f2,  f2,  1.0f);
                int l = 0;
                for (int i2 = 0; i2 + this.scrollPos < this.drawnChatLines.size() && i2 < i; ++i2) {
                    final ChatLine chatline = this.drawnChatLines.get(i2 + this.scrollPos);
                    if (chatline != null) {
                        final int j2 = updateCounter - chatline.getUpdatedCounter();
                        if (j2 < 200 || flag) {
                            double d0 = j2 / 200.0;
                            d0 = 1.0 - d0;
                            d0 *= 10.0;
                            d0 = MathHelper.clamp(d0,  0.0,  1.0);
                            d0 *= d0;
                            int l2 = (int)(255.0 * d0);
                            if (flag) {
                                l2 = 255;
                            }
                            l2 *= (int)f;
                            ++l;
                            if (l2 > 3) {
                                final int i3 = 0;
                                final int j3 = -i2 * 9;
                                if (!BetterChat.getSettings().clear) {
                                    drawRect(-2,  j3 - 9,  i3 + k + 4,  j3,  l2 / 2 << 24);
                                }
                                final String s = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                if (BetterChat.getSettings().smooth && i2 <= GuiBetterChat.newLines) {
                                    this.mc.fontRenderer.drawStringWithShadow(s,  0.0f,  (float)(j3 - 8),  16777215 + ((int)(l2 * percent) << 24));
                                }
                                else {
                                    this.mc.fontRenderer.drawStringWithShadow(s,  (float)i3,  (float)(j3 - 8),  16777215 + (l2 << 24));
                                }
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }
                if (flag) {
                    final int k2 = this.mc.fontRenderer.FONT_HEIGHT;
                    GlStateManager.translate(-3.0f,  0.0f,  0.0f);
                    final int l3 = j * k2 + j;
                    final int i4 = l * k2 + l;
                    final int j4 = this.scrollPos * i4 / j;
                    final int k3 = i4 * i4 / l3;
                    if (l3 != i4) {
                        final int k4 = (j4 > 0) ? 170 : 96;
                        final int l4 = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0,  -j4,  2,  -j4 - k3,  l4 + (k4 << 24));
                        drawRect(2,  -j4,  1,  -j4 - k3,  13421772 + (k4 << 24));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }
    
    public void clearChatMessages(final boolean p_146231_1_) {
        this.drawnChatLines.clear();
        this.chatLines.clear();
        if (p_146231_1_) {
            this.sentMessages.clear();
        }
    }
    
    public void printChatMessage(final ITextComponent chatComponent) {
        this.printChatMessageWithOptionalDeletion(chatComponent,  0);
    }
    
    public void printChatMessageWithOptionalDeletion(final ITextComponent chatComponent,  final int chatLineId) {
        GuiBetterChat.percentComplete = 0.0f;
        this.setChatLine(chatComponent,  chatLineId,  this.mc.ingameGUI.getUpdateCounter(),  false);
        GuiBetterChat.LOGGER.info("[CHAT] {}",  (Object)chatComponent.getUnformattedText().replaceAll("\r",  "\\\\r").replaceAll("\n",  "\\\\n"));
    }
    
    private void setChatLine(final ITextComponent chatComponent,  final int chatLineId,  final int updateCounter,  final boolean displayOnly) {
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }
        final int i = MathHelper.floor(this.getChatWidth() / this.getChatScale());
        final List<ITextComponent> list = GuiUtilRenderComponents.splitText(chatComponent,  i,  this.mc.fontRenderer,  false,  false);
        final boolean flag = this.getChatOpen();
        GuiBetterChat.newLines = list.size() - 1;
        for (final ITextComponent itextcomponent : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }
            this.drawnChatLines.add(0,  new ChatLine(updateCounter,  itextcomponent,  chatLineId));
        }
        while (this.drawnChatLines.size() > 100) {
            this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
        }
        if (!displayOnly) {
            this.chatLines.add(0,  new ChatLine(updateCounter,  chatComponent,  chatLineId));
            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }
    
    public void refreshChat() {
        this.drawnChatLines.clear();
        this.resetScroll();
        for (int i = this.chatLines.size() - 1; i >= 0; --i) {
            final ChatLine chatline = this.chatLines.get(i);
            this.setChatLine(chatline.getChatComponent(),  chatline.getChatLineID(),  chatline.getUpdatedCounter(),  true);
        }
    }
    
    public List<String> getSentMessages() {
        return this.sentMessages;
    }
    
    public void addToSentMessages(final String message) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(message)) {
            this.sentMessages.add(message);
        }
    }
    
    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }
    
    public void scroll(final int amount) {
        this.scrollPos += amount;
        final int i = this.drawnChatLines.size();
        if (this.scrollPos > i - this.getLineCount()) {
            this.scrollPos = i - this.getLineCount();
        }
        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }
    
    @Nullable
    public ITextComponent getChatComponent(final int mouseX,  final int mouseY) {
        if (!this.getChatOpen()) {
            return null;
        }
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        final int i = scaledresolution.getScaleFactor();
        final float f = this.getChatScale();
        int j = mouseX / i - 2 - BetterChat.getSettings().xOffset;
        int k = mouseY / i - 40 + BetterChat.getSettings().yOffset;
        j = MathHelper.floor(j / f);
        k = MathHelper.floor(k / f);
        if (j < 0 || k < 0) {
            return null;
        }
        final int l = Math.min(this.getLineCount(),  this.drawnChatLines.size());
        if (j <= MathHelper.floor(this.getChatWidth() / this.getChatScale()) && k < this.mc.fontRenderer.FONT_HEIGHT * l + l) {
            final int i2 = k / this.mc.fontRenderer.FONT_HEIGHT + this.scrollPos;
            if (i2 >= 0 && i2 < this.drawnChatLines.size()) {
                final ChatLine chatline = this.drawnChatLines.get(i2);
                int j2 = 0;
                for (final ITextComponent itextcomponent : chatline.getChatComponent()) {
                    if (itextcomponent instanceof TextComponentString) {
                        j2 += this.mc.fontRenderer.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(((TextComponentString)itextcomponent).getText(),  false));
                        if (j2 > j) {
                            return itextcomponent;
                        }
                        continue;
                    }
                }
            }
            return null;
        }
        return null;
    }
    
    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GuiChat;
    }
    
    public void deleteChatLine(final int id) {
        Iterator<ChatLine> iterator = this.drawnChatLines.iterator();
        while (iterator.hasNext()) {
            final ChatLine chatline = iterator.next();
            if (chatline.getChatLineID() == id) {
                iterator.remove();
            }
        }
        iterator = this.chatLines.iterator();
        while (iterator.hasNext()) {
            final ChatLine chatline2 = iterator.next();
            if (chatline2.getChatLineID() == id) {
                iterator.remove();
                break;
            }
        }
    }
    
    public int getChatWidth() {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }
    
    public int getChatHeight() {
        return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }
    
    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }
    
    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GuiBetterChat.prevMillis = -1L;
    }
}
