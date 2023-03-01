package com.me.infinity.loop.features.gui.screen.taskbar.element;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.gui.screen.DrawableComponent;
import com.me.infinity.loop.util.utils.renders.builder.Render2DBuilder;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDate extends DrawableComponent {
    @Override
    public void drawComponent() {
        // time and date
        Render2DBuilder.prepareScale(1.5f, 1.5f);
        String time = ChatFormatting.WHITE + (new SimpleDateFormat("h:mm:s a")).format(new Date());
        InfinityLoop.textManager.drawString(time, 3, 2, -1, true);

    }
}
