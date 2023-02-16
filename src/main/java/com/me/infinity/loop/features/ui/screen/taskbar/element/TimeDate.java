package com.me.infinity.loop.features.ui.screen.taskbar.element;

import com.me.infinity.loop.features.ui.screen.DrawableComponent;
import com.me.infinity.loop.util.renders.builder.Render2DBuilder;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDate extends DrawableComponent {
    @Override
    public void drawComponent() {
        // time and date
        Render2DBuilder.prepareScale(1.5f, 1.5f);
        {
            String time = ChatFormatting.WHITE + (new SimpleDateFormat("h:mm:s a")).format(new Date());
            mc.fontRenderer.drawString(time, 3, 2, -1);

        }
        Render2DBuilder.prepareScale(1.5f, 2.5f);
        {
            String date = ChatFormatting.WHITE + (new SimpleDateFormat("d/m/y")).format(new Date());
            mc.fontRenderer.drawString(date, 3, 4, -1);
        }
    }
}
