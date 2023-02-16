package com.me.infinity.loop.features.modules.render;

import com.me.infinity.loop.event.events.PacketEvent;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.interfaces.Util;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Animation extends Module {
    private static Animation INSTANCE = new Animation();
    public Setting<Mode> mode = this.register(new Setting("OldAnimations", Mode.NoDelay));
    public Setting<WhatHand> whatHand = this.register(new Setting<>("Swing", WhatHand.Mainhand));
    public Setting<Boolean> swing = register(new Setting<>("customSwingAnimation", false));
    public Setting<Integer> speed = register(new Setting<>("Speed", 13, 1, 20,v -> this.swing.getValue()));

    public Animation() {
        super("Animation", "Change animations.", Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static Animation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Animation();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (this.whatHand.getValue() == WhatHand.Offhand) {
            Animation.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (this.mode.getValue() == Mode.NoDelay && Animation.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            Animation.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            Animation.mc.entityRenderer.itemRenderer.itemStackMainHand = Animation.mc.player.getHeldItemMainhand();
        }

    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send send) {
        final Object t = send.getPacket();
        if (t instanceof CPacketAnimation && this.whatHand.getValue() == WhatHand.Disable) {
            send.setCanceled(true);
        }
    }

    private enum Mode
    {
        Normal,
        NoDelay
    }

    private enum WhatHand
    {
        Mainhand,
        Offhand,
        Disable
    }
}

