package com.me.infinity.loop.features.gui.screen.windows.window;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.gui.font.FontRender;
import com.me.infinity.loop.features.gui.screen.windows.window.parts.FriendPart;
import com.me.infinity.loop.features.modules.client.ClickGui;
import com.me.infinity.loop.util.renders.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import static com.me.infinity.loop.features.gui.screen.components.items.buttons.StringButton.removeLastChar;
import static com.me.infinity.loop.util.interfaces.Util.mc;

public class WindowFriends {
    private static int posX;
    private static int posY;
    private static int width = 300;
    private static int height = 200;

    private static boolean listeningString = false;

    private static boolean drag = false;
    private static int dragX, dragY;

    private static boolean rescale = false;
    private static int rescaleX, rescaleY;


    private static int dwheel;

    private static int friendPartId = 0;

    static String header = "Friend Manager";

    static String addString = "Type here";



    static ArrayList<FriendPart> friends = new ArrayList<FriendPart>();
    static ArrayList<String> already = new ArrayList<String>();

    public static void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        int color = new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue()).getRGB();
        if(drag){
            posX = mouseX - dragX;
            posY = mouseY - dragY;
            friends.forEach(friendPart -> friendPart.setPosX(mouseX - dragX));
            friends.forEach(friendPart -> friendPart.setPosY(mouseY - dragY));
        }
        if(rescale){
            width = mouseX - rescaleX;
            height = mouseY - rescaleY;
            friends.forEach(friendPart -> friendPart.setWidth(mouseX - rescaleX));
        }

        if(!listeningString){
            addString = "Type here";
        }


        //base
        RenderUtil.drawSmoothRect(posX,posY,posX + width,posY + height, new Color(0xF21A1A1A, true).getRGB());

        //border
        RenderUtil.drawRect(posX + 5,posY + 9,posX + width - 5f,posY + 11f, new Color(0x2CFFFFFF, true).getRGB());

        //Heather
        FontRender.drawCentString5(header, (posX + (posX + width)) / 2f,posY  + FontRender.getFontHeight5() - 2, new Color(0xC7FFFFFF, true).getRGB());

        //line
        RenderUtil.drawRect(posX + 5,posY + height  - 20,posX + width - 5f,posY + height  - 5, new Color(0x32FFFFFF, true).getRGB());
        FontRender.drawString5(addString + (listeningString ? "_" : ""), posX + 10,posY + height  - 18 + 5, new Color(0xEAFFFDFD, true).getRGB());

        //button
        if(isHoveringAddButton(mouseX,mouseY)) {
            RenderUtil.drawRect(posX + width - 7f - 40f, posY + height - 18, posX + width - 7f, posY + height - 7, new Color(color, true).getRGB());
            FontRender.drawCentString5("ADD", ((posX + width - 7f - 40f) + (posX + width - 7f)) / 2f, posY + height - 18 + 5, new Color(0x99FFFFFF, true).getRGB());
        } else {
            RenderUtil.drawRect(posX + width - 7f - 40f, posY + height - 18, posX + width - 7f, posY + height - 7, new Color(color, true).getRGB());
            FontRender.drawCentString5("ADD", ((posX + width - 7f - 40f) + (posX + width - 7f)) / 2f, posY + height - 18 + 5, new Color(0x69FFFFFF, true).getRGB());
        }

        for (String friend : InfinityLoop.friendManager.getFriends()) {
            if(already.contains(friend)){
                continue;
            }
            friends.add(new FriendPart(friend, posX, posY,width,friendPartId));
            already.add(friend);
            ++friendPartId;
        }

        RenderUtil.glScissor(posX,posY + 12f,posX + width,posY + height  - 25,sr);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        friends.forEach(friendPart -> friendPart.renderPart(mouseX,mouseY));
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        friends.forEach(friendPart -> friendPart.setDwheel(dwheel));
        if(isInWindow(mouseX,mouseY)) {
            checkMouseWheel();
        }
    }

    public static void keyTyped(char typedChar, int keyCode) {
        if (listeningString){

            if(keyCode == 42){
                return;
            }

            if(keyCode == 54){
                return;
            }

            if(keyCode == 1){
                addString = "Type here";
                listeningString = false;
                return;
            }
            if(keyCode == 14){
                addString = removeLastChar(addString);
                return;
            }
            if(keyCode == 28){
                InfinityLoop.friendManager.addFriend(addString);
                addString = "Type here";
                listeningString = false;
                return;
            }
            addString = addString + typedChar;
        }

    }

    public static void mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(isHoveringHeader(mouseX,mouseY)){
            drag = true;
            dragX = mouseX - posX;
            dragY = mouseY - posY;
        }
        if(isHoveringCorner(mouseX,mouseY)){
            rescale = true;
            rescaleX = mouseX - width;
            rescaleY = mouseY - height;
        }
        if(isHoveringString(mouseX,mouseY)){
            addString = "";
            listeningString = true;
        }
        if(isHoveringAddButton(mouseX,mouseY)){
            if(Objects.equals(addString, "Type here")){
                return;
            }
            InfinityLoop.friendManager.addFriend(addString);
            listeningString = false;
        }
        if(isInWindow(mouseX,mouseY)){
            friends.forEach(friendPart -> friendPart.mouseClicked(mouseX,mouseY,mouseButton));
            if(isHoveringDelButton(mouseX)){
                reset();
            }
        }
    }

    public static void mouseReleased(int mouseX, int mouseY, int state) {
        drag = rescale = false;
    }

    static boolean isHoveringHeader(int x, int y){
        return x > posX && x < posX + width && y > posY && y < posY + 20;
    }

    static boolean isHoveringString(int x, int y){
        return x > posX + 5 && x < posX + width - 7f - 40f && y > posY + height  - 20 && y < posY + height  - 5;
    }

    static boolean isHoveringAddButton(int x, int y){
        return x > posX + width - 7f - 40f && x < posX + width - 7f && y > posY + height  - 18 && y < posY + height  - 7;
    }

    static boolean isInWindow(int x, int y){
        return x > posX && x < posX + width && y > posY + 12f && y < posY + height  - 25;
    }

    static boolean isHoveringDelButton(int x){
        return x > posX + width - 20 && x < posX + width - 10;
    }

    static boolean isHoveringCorner(int x, int y){
        return x > posX + width - 5 && x < posX + width && y > posY + height - 5 && y < posY + height;
    }

    public static void reset(){
        already.clear();
        friends.clear();
        friendPartId = 0;
    }

    public static void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            dwheel -= 10;
        } else if (dWheel > 0) {
            dwheel += 10;
        }
    }
}
