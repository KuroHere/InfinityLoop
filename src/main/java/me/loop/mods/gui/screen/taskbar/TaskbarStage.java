package me.loop.mods.gui.screen.taskbar;

import me.loop.mods.gui.screen.taskbar.element.Taskbar;
import me.loop.mods.gui.screen.taskbar.element.TimeDate;
import me.loop.mods.gui.screen.DrawableComponent;

public class TaskbarStage extends DrawableComponent {
    private final Taskbar taskbar = new Taskbar();
    private final TimeDate timeDate = new TimeDate();
    @Override
    public void drawComponent() {
        taskbar.drawComponent();
        timeDate.drawComponent();
    }
}
