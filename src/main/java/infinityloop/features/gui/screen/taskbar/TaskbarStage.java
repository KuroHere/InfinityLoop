package infinityloop.features.gui.screen.taskbar;

import infinityloop.features.gui.screen.DrawableComponent;
import infinityloop.features.gui.screen.taskbar.element.Taskbar;
import infinityloop.features.gui.screen.taskbar.element.TimeDate;

public class TaskbarStage extends DrawableComponent {
    private final Taskbar taskbar = new Taskbar();
    private final TimeDate timeDate = new TimeDate();
    @Override
    public void drawComponent() {
        taskbar.drawComponent();
        timeDate.drawComponent();
    }
}
