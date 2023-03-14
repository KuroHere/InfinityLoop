package me.loop.mods.modules.impl.render.motionblur;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Locale;

class MotionBlurResource implements IResource
{

    @Override
    public ResourceLocation getResourceLocation() {
        return null;
    }

    public InputStream getInputStream() {
        final double amount = 0.7 + MotionBlur.getInstance().blurAmount.getValue() / 100.0 * 3.0 - 0.01;
        return IOUtils.toInputStream(String.format(Locale.ENGLISH, "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}", amount, amount, amount));
    }

    public boolean hasMetadata() {
        return false;
    }

    @Nullable
    public <T extends IMetadataSection> T getMetadata(final String s) {
        return null;
    }

    @Override
    public String getResourcePackName() {
        return null;
    }

    public void close() {
    }
}
