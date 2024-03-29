package me.loop.mods.gui.screen.effects.particles;

import me.loop.mods.modules.impl.client.ClickGui.ClickGui;
import me.loop.mods.modules.impl.client.Colors;
import me.loop.mods.gui.InfinityLoopGui;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.api.utils.impl.maths.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import javax.vecmath.Vector2f;
import java.awt.*;

public final class ParticleSystem {

    private final int PARTS = 100;
    private final Particle[] particles = new Particle[PARTS];

    private ScaledResolution scaledResolution;
    private int alpha;

    public ParticleSystem(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
        for (int i = 0; i < PARTS; i++) {
            this.particles[i] = new Particle(new Vector2f((float) (Math.random() * scaledResolution.getScaledWidth()), (float) (Math.random() * scaledResolution.getScaledHeight())));
        }
    }

    public void update() {
        for (int i = 0; i < PARTS; i++) {
            final Particle particle = this.particles[i];
            if (this.scaledResolution != null) {
                final boolean isOffScreenX = particle.getPos().x > this.scaledResolution.getScaledWidth() || particle.getPos().x < 0;
                final boolean isOffScreenY = particle.getPos().y > this.scaledResolution.getScaledHeight() || particle.getPos().y < 0;
                if (isOffScreenX || isOffScreenY) {
                    particle.respawn(this.scaledResolution);
                }
            }
            particle.update();
        }
    }

    public void render(int mouseX, int mouseY) {
        final boolean isInHudEditor = Minecraft.getMinecraft().currentScreen instanceof InfinityLoopGui && Minecraft.getMinecraft().player != null;

        for (int i = 0; i < PARTS; i++) {
            final Particle particle = this.particles[i];

            if (isInHudEditor) {
                for (int j = 1; j < PARTS; j++) {
                    if (i != j) {
                        final Particle otherParticle = this.particles[j];
                        final Vector2f diffPos = new Vector2f(particle.getPos());
                        diffPos.sub(otherParticle.getPos());
                        final float diff = diffPos.length();
                        final int distance = ClickGui.INSTANCE.particleLength.getValue() / (scaledResolution.getScaleFactor() <= 1 ? 3 : scaledResolution.getScaleFactor());
                        if (diff < distance) {
                            final int lineAlpha = (int) MathUtil.map(diff, distance, 0, 0, 127);
                            if (lineAlpha > 8) {
                                if (Colors.getInstance().rainbow.getValue() && ClickGui.INSTANCE.colorSync.getValue()) {
                                    RenderUtil.drawLine(particle.getPos().x + particle.getSize() / 2.0f, particle.getPos().y + particle.getSize() / 2.0f, otherParticle.getPos().x + otherParticle.getSize() / 2.0f, otherParticle.getPos().y + otherParticle.getSize() / 2.0f, 1.0f, ColorUtil.changeAlpha(0xFF9900EE, this.alpha));
                                } else {
                                    RenderUtil.drawLine(particle.getPos().x + particle.getSize() / 2.0f, particle.getPos().y + particle.getSize() / 2.0f, otherParticle.getPos().x + otherParticle.getSize() / 2.0f, otherParticle.getPos().y + otherParticle.getSize() / 2.0f, 1.0f, new Color(ClickGui.INSTANCE.moduleMainC.getValue().getRed(), ClickGui.INSTANCE.moduleMainC.getValue().getGreen(), ClickGui.INSTANCE.moduleMainC.getValue().getBlue()).getRGB());
                                }
                            }
                        }
                    }
                }
            }

            particle.render(mouseX, mouseY);
        }
    }


    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public void setScaledResolution(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
}
