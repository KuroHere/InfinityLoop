



package me.loop.features.gui.other.chat.utils;

public class AnimationTools
{
    public static float clamp(final float number,  final float min,  final float max) {
        return (number < min) ? min : Math.min(number,  max);
    }
}
