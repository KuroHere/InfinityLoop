package me.loop.client.gui.screen.taskbar;

import me.loop.client.gui.screen.taskbar.element.Taskbar;
import me.loop.client.gui.screen.taskbar.element.TimeDate;
import me.loop.client.gui.screen.DrawableComponent;

public class TaskbarStage extends DrawableComponent {
    private final Taskbar taskbar = new Taskbar();
    private final TimeDate timeDate = new TimeDate();
    @Override
    public void drawComponent() {
        taskbar.drawComponent();
        timeDate.drawComponent();
    }
}