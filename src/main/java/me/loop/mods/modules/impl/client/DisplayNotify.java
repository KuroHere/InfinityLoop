package me.loop.mods.modules.impl.client;

import me.loop.api.events.impl.TotemPopEvent;
import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.events.impl.render.Render2DEvent;
import me.loop.mods.gui.other.notifications.Notification;
import me.loop.mods.modules.Category;
import me.loop.mods.modules.Module;
import me.loop.mods.settings.Setting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

public class DisplayNotify
        extends Module {
    public static DisplayNotify INSTANCE;
    ArrayList<Notification> notifications = new ArrayList<>();
    private final Setting<NotifyPage> page = this.add(new Setting<NotifyPage>("NotifyPage", NotifyPage.TotemPops));
    public Setting<Integer> displayTime = this.add(new Setting<Object>("DisplayTime", 1500, 1000, 3000));

    public Setting<Boolean> pop = this.add(new Setting<Boolean>("TotemPopsNotify", true, v -> this.page.getValue() == NotifyPage.TotemPops));
    public Setting<Integer> tpRed = this.add(new Setting<Object>("TotemPopSideRed", 255, 0, 255, v -> this.page.getValue() == NotifyPage.TotemPops && this.pop.getValue()));
    public Setting<Integer> tpGreen = this.add(new Setting<Object>("TotemPopSideGreen", 255, 0, 255, v -> this.page.getValue() == NotifyPage.TotemPops && this.pop.getValue()));
    public Setting<Integer> tpBlue = this.add(new Setting<Object>("TotemPopSideBlue", 50, 0, 255, v -> this.page.getValue() == NotifyPage.TotemPops && this.pop.getValue()));

    public Setting<Boolean> enable = this.add(new Setting<Boolean>("EnableNotify", true, v -> this.page.getValue() == NotifyPage.Enable));
    public Setting<Integer> eRed = this.add(new Setting<Object>("EnableSideRed", 50, 0, 255, v -> this.page.getValue() == NotifyPage.Enable && this.enable.getValue()));
    public Setting<Integer> eGreen = this.add(new Setting<Object>("EnableSideGreen", 255, 0, 255, v -> this.page.getValue() == NotifyPage.Enable && this.enable.getValue()));
    public Setting<Integer> eBlue = this.add(new Setting<Object>("EnableBlue", 50, 0, 255, v -> this.page.getValue() == NotifyPage.Enable && this.enable.getValue()));

    public Setting<Boolean> disable = this.add(new Setting<Boolean>("DisableNotify", true, v -> this.page.getValue() == NotifyPage.Disable));
    public Setting<Integer> dRed = this.add(new Setting<Object>("DisableSideRed", 255, 0, 255, v -> this.page.getValue() == NotifyPage.Disable && this.disable.getValue()));
    public Setting<Integer> dGreen = this.add(new Setting<Object>("DisableSideGreen", 50, 0, 255, v -> this.page.getValue() == NotifyPage.Disable && this.disable.getValue()));
    public Setting<Integer> dBlue = this.add(new Setting<Object>("DisableSideBlue", 50, 0, 255, v -> this.page.getValue() == NotifyPage.Disable && this.disable.getValue()));

    public Setting<Integer> bgRed = this.add(new Setting<Object>("BackgroundRed", 50, 0, 255));
    public Setting<Integer> bgGreen = this.add(new Setting<Object>("BackgroundGreen", 50, 0, 255));
    public Setting<Integer> bgBlue = this.add(new Setting<Object>("BackgroundBlue", 50, 0, 255));

    public Setting<Integer> tRed = this.add(new Setting<Object>("TitleRed", 255, 0, 255));
    public Setting<Integer> tGreen = this.add(new Setting<Object>("TitleGreen", 255, 0, 255));
    public Setting<Integer> tBlue = this.add(new Setting<Object>("TitleBlue", 255, 0, 255));

    public Setting<Integer> descRed = this.add(new Setting<Object>("DescriptionRed", 170, 0, 255));
    public Setting<Integer> descGreen = this.add(new Setting<Object>("DescriptionGreen", 170, 0, 255));
    public Setting<Integer> descBlue = this.add(new Setting<Object>("DescriptionBlue", 170, 0, 255));

    public DisplayNotify() {
        super("DisplayNotify", "notifications for stuffs.", Category.CLIENT);
        INSTANCE = this;
    }

    public static DisplayNotify getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DisplayNotify();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        notifications.add(new Notification("Test Notify", "Notification Test", new Color(255, 255, 255), this.displayTime.getValue()));
    }

    @SubscribeEvent
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) return;
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int y = scaledResolution.getScaledHeight() - 30;
        ArrayList<Notification>remove=new ArrayList<>();
        for (Notification notification : notifications) {
            if (!notification.ended()) {
                notification.render(y); y -= 40;
            } else {
                remove.add(notification);
            }
        }
        notifications.removeAll(remove);
    }

    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (event.getEntity() != mc.player) {
            if (this.pop.getValue().booleanValue()) {
                notifications.add(new Notification("Totem pop", event.getEntity().getName() + " popped a totem.", new Color(this.tpRed.getValue(), this.tpGreen.getValue(), this.tpBlue.getValue()), this.displayTime.getValue()));
            }
        }
    }

    @SubscribeEvent
    public void onModuleEvent(ClientEvent event) {
        if (event.getStage() == 0) {
            if (this.disable.getValue().booleanValue()) {
                notifications.add(new Notification("Module Disabled", event.getFeature().getName() + " disabled.", new Color(this.dRed.getValue(), this.dGreen.getValue(), this.dBlue.getValue()), this.displayTime.getValue()));
            }
        }
        if (event.getStage() == 1) {
            if (this.enable.getValue().booleanValue()) {
                notifications.add(new Notification("Module Enabled", event.getFeature().getName() + " enabled.", new Color(this.eRed.getValue(), this.eGreen.getValue(), this.eBlue.getValue()), this.displayTime.getValue()));
            }
        }
    }

    public enum NotifyPage {
        TotemPops,
        Enable,
        Disable;
    }
}
