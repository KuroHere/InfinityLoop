package me.loop.mods.modules.impl.render.motionblur;

import net.minecraft.util.*;
import net.minecraft.client.resources.*;
import java.util.*;

class MotionBlurResourceManager implements IResourceManager
{
    public Set<String> getResourceDomains() {
        return null;
    }

    public IResource getResource(final ResourceLocation resourceLocation) {
        return (IResource)new MotionBlurResource();
    }

    public List<IResource> getAllResources(final ResourceLocation resourceLocation) {
        return null;
    }
}
