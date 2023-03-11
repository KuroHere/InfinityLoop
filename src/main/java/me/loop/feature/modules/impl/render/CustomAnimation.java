package me.loop.feature.modules.impl.render;

import me.loop.api.events.impl.network.EventPacket;
import me.loop.feature.modules.Module;
import me.loop.feature.modules.Category;
import me.loop.feature.modules.settings.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomAnimation extends Module {
    private static CustomAnimation INSTANCE = new CustomAnimation();
    public Setting<Boolean> playersDisableAnimations  = add(new Setting<>("DisableAnimations", false));

    public Setting<Mode> mode = this.add(new Setting("OldAnimations", Mode.NoDelay));
    public Setting<WhatHand> whatHand = this.add(new Setting<>("Swing", WhatHand.Mainhand));
    public Setting<Boolean> swing = add(new Setting<>("customSwingAnimation", false));
    public Setting<Integer> speed = add(new Setting<>("Speed", 13, 1, 20,v -> this.swing.getValue()));

    public CustomAnimation() {
        super("Animation", "Change animations.", Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static CustomAnimation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomAnimation();
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
        if (playersDisableAnimations.getValue()) {
            for (EntityPlayer player : CustomAnimation.mc.world.playerEntities) {
                player.limbSwing = Float.intBitsToFloat(Float.floatToIntBits(1.8755627E38f) ^ 0x7F0D1A06);
                player.limbSwingAmount = Float.intBitsToFloat(Float.floatToIntBits(6.103741E37f) ^ 0x7E37AD83);
                player.prevLimbSwingAmount = Float.intBitsToFloat(Float.floatToIntBits(4.8253957E37f) ^ 0x7E11357F);
            }
        }
        if (this.whatHand.getValue() == WhatHand.Offhand) {
            CustomAnimation.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (this.mode.getValue() == Mode.NoDelay && CustomAnimation.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            CustomAnimation.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            CustomAnimation.mc.entityRenderer.itemRenderer.itemStackMainHand = CustomAnimation.mc.player.getHeldItemMainhand();
        }
        if (this.whatHand.getValue().equals((Object) WhatHand.PacketSwing) && CustomAnimation.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && (double) CustomAnimation.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            CustomAnimation.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            CustomAnimation.mc.entityRenderer.itemRenderer.itemStackMainHand = CustomAnimation.mc.player.getHeldItemMainhand();
        }
    }

    @SubscribeEvent
    public void onPacketSend(final EventPacket.Send send) {
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
        Disable,
        PacketSwing
    }
}

