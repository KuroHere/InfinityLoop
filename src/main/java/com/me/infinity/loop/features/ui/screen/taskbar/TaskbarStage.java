package com.me.infinity.loop.features.ui.screen.taskbar;

import com.me.infinity.loop.features.ui.screen.DrawableComponent;
import com.me.infinity.loop.features.ui.screen.taskbar.element.Taskbar;
import com.me.infinity.loop.features.ui.screen.taskbar.element.TimeDate;

public class TaskbarStage extends DrawableComponent {
    private final Taskbar taskbar = new Taskbar();
    private final TimeDate timeDate = new TimeDate();
    @Override
    public void drawComponent() {
        taskbar.drawComponent();
        timeDate.drawComponent();
    }
}
