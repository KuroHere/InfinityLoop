package me.loop.features.gui.screen.taskbar;

import me.loop.features.gui.screen.DrawableComponent;
import me.loop.features.gui.screen.taskbar.element.Taskbar;
import me.loop.features.gui.screen.taskbar.element.TimeDate;

public class TaskbarStage extends DrawableComponent {
    private final Taskbar taskbar = new Taskbar();
    private final TimeDate timeDate = new TimeDate();
    @Override
    public void drawComponent() {
        taskbar.drawComponent();
        timeDate.drawComponent();
    }
}
