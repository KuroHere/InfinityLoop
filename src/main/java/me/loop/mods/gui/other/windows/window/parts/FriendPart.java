package me.loop.mods.gui.other.windows.window.parts;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.api.utils.impl.renders.shaders.PNGtoResourceLocation;
import me.loop.mods.gui.font.FontRender;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static me.loop.api.utils.impl.Util.mc;
import static me.loop.api.utils.impl.renders.RenderUtil.drawCompleteImage;
import static me.loop.api.utils.impl.renders.RenderUtil.drawImage;

public class FriendPart{

    public String name;

    private int posX;
    private int posY;
    private int width;
    private final int id;
    private int dwheel;


    ResourceLocation head;
    ResourceLocation crackedSkin = new ResourceLocation("loop:imgs/cracked.png");
    ResourceLocation bin = new ResourceLocation("loop:imgs/trashbinnigga.png");

    public FriendPart(String name,int posX,int posY, int width,int id){
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.id = id;
        head = PNGtoResourceLocation.getTexture2(name, "png");
    }

    public void renderPart(int x,int y){
        RenderUtil.drawSmoothRect(posX + 5,posY + 20 + 23*id + dwheel,posX + width - 5, posY + 40 + 23*id + dwheel,new Color(0xBDFFFFFF, true).getRGB());
        RenderUtil.drawSmoothRect(posX + width - 20, posY + 25 + 23 * id + dwheel, posX + width - 10, posY + 35 + 23 * id + dwheel, isHoveringDelButton(x,y) ? new Color(0xD50022A0, true).getRGB(): new Color(0x990022A8, true).getRGB());

        drawImage(bin, posX + width - 18, posY + 27 + 23*id + dwheel, 6, 6,isHoveringDelButton(x,y) ?new Color(0xFFFFFFFF, true)  : new Color(0xB3FFFFFF, true));


        if(head != null){
            GlStateManager.color(1f,1f,1f,1f);
            mc.getTextureManager().bindTexture(head);
            drawCompleteImage(posX + 7,posY + 20 + 23*id + dwheel,18, 18);
        } else {
            GlStateManager.color(1f,1f,1f,1f);
            mc.getTextureManager().bindTexture(crackedSkin);
            drawCompleteImage(posX + 7,posY + 20 + 23*id + dwheel,18, 18);
        }
        FontRender.drawString(name + "   " + (checkOnline(name) ? ChatFormatting.GREEN + "Online" : ChatFormatting.GRAY + "Offline"),posX + 7 + 22, posY + 20 + 23*id + dwheel, new Color(0x343434).getRGB());

    }


    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isHoveringDelButton(mouseX,mouseY)){
            Managers.friendManager.removeFriend(name);
        }
    }

    public boolean checkOnline(String name){
        if(mc.player.connection.getPlayerInfo(name) != null){
            return true;
        } else {
            return false;
        }
    }


    boolean isHoveringDelButton(int x, int y){
        return x > posX + width - 20 && x < posX + width - 10 && y > posY + 25 + 23*id + dwheel && y < posY + 35 + 23*id + dwheel;
    }

    public void setDwheel(int dw) {
        dwheel = dw;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }
}