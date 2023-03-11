package me.loop.feature.modules.impl.render;

import me.loop.api.events.impl.render.Render2DEvent;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.feature.modules.Category;
import me.loop.feature.modules.Module;
import me.loop.feature.modules.settings.Setting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public final class HitMarkers
        extends Module {
    public final ResourceLocation image;
    private static final ResourceLocation codHitmarker = new ResourceLocation("Loop", "sounds/cod_hitmarker");
    private static final ResourceLocation csgoHitmarker = new ResourceLocation("Loop", "sounds/csgo_hitmarker");
    private final Setting<Sound> sound = this.add(new Setting<Object>("Sound", (Object)Sound.NONE));
    public Setting<Color> colorC = this.add(new Setting<Color>("MarkerColor", new Color(255, 255, 255, 255)));
    public Setting<Integer> thickness = this.add(new Setting<Integer>("Thickness", 2, 1, 6));
    public Setting<Double> time = this.add(new Setting<Double>("Time", 20.0, 1.0, 50.0));
    public static final SoundEvent COD_EVENT = new SoundEvent(codHitmarker);
    public static final SoundEvent CSGO_EVENT = new SoundEvent(csgoHitmarker);
    private int renderTicks = 100;

    public HitMarkers() {
        super("HitMarkers", "hitmarker thingys", Category.RENDER, false, false, false);
        this.image = new ResourceLocation("textures/hitmarker.png");
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if ((double)this.renderTicks < this.time.getValue()) {
            ScaledResolution resolution = new ScaledResolution(mc);
            this.drawHitMarkers();
        }
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (!event.getEntity().equals((Object)HitMarkers.mc.player)) {
            return;
        }
        this.renderTicks = 0;
    }

    @SubscribeEvent
    public void onTickClientTick(TickEvent event) {
        ++this.renderTicks;
    }

    public void drawHitMarkers() {
        ScaledResolution resolution = new ScaledResolution(mc);
        RenderUtil.drawLine((float)resolution.getScaledWidth() / 2.0f - 4.0f, (float)resolution.getScaledHeight() / 2.0f - 4.0f, (float)resolution.getScaledWidth() / 2.0f - 8.0f, (float)resolution.getScaledHeight() / 2.0f - 8.0f, this.thickness.getValue().intValue(), new Color(this.colorC.getValue().getRed(), this.colorC.getValue().getGreen(), this.colorC.getValue().getBlue()).getRGB());
        RenderUtil.drawLine((float)resolution.getScaledWidth() / 2.0f + 4.0f, (float)resolution.getScaledHeight() / 2.0f - 4.0f, (float)resolution.getScaledWidth() / 2.0f + 8.0f, (float)resolution.getScaledHeight() / 2.0f - 8.0f, this.thickness.getValue().intValue(), new Color(this.colorC.getValue().getRed(), this.colorC.getValue().getGreen(), this.colorC.getValue().getBlue()).getRGB());
        RenderUtil.drawLine((float)resolution.getScaledWidth() / 2.0f - 4.0f, (float)resolution.getScaledHeight() / 2.0f + 4.0f, (float)resolution.getScaledWidth() / 2.0f - 8.0f, (float)resolution.getScaledHeight() / 2.0f + 8.0f, this.thickness.getValue().intValue(), new Color(this.colorC.getValue().getRed(), this.colorC.getValue().getGreen(), this.colorC.getValue().getBlue()).getRGB());
        RenderUtil.drawLine((float)resolution.getScaledWidth() / 2.0f + 4.0f, (float)resolution.getScaledHeight() / 2.0f + 4.0f, (float)resolution.getScaledWidth() / 2.0f + 8.0f, (float)resolution.getScaledHeight() / 2.0f + 8.0f, this.thickness.getValue().intValue(), new Color(this.colorC.getValue().getRed(), this.colorC.getValue().getGreen(), this.colorC.getValue().getBlue()).getRGB());
    }

    public enum Sound {
        NONE,
        COD,
        CSGO;

    }
}
