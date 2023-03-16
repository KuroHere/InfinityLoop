package me.loop.api.managers;

import me.loop.mods.Mod;

public class TimerManager
        extends Mod {
    private float timer = 1.0f;

    public void set(float factor) {

        if (factor < 0.1f) factor = 0.1f;

        timer = factor;
    }

    public void reset() {
        timer = 1;
    }

    public float get() {
        return timer;
    }
}
