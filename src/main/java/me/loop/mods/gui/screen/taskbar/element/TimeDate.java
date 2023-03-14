package me.loop.mods.gui.screen.taskbar.element;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.renders.builder.Render2DBuilder;
import me.loop.mods.gui.screen.DrawableComponent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDate extends DrawableComponent {
    @Override
    public void drawComponent() {
        // time and date
        Render2DBuilder.prepareScale(1.5f, 1.5f);
        String time = ChatFormatting.WHITE + (new SimpleDateFormat("h:mm:s a")).format(new Date());
        Managers.textManager.drawString(time, 3, 2, -1, true);

    }
}

