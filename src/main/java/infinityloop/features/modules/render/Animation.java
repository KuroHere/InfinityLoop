package infinityloop.features.modules.render;

import infinityloop.event.events.network.EventPacket;
import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Animation extends Module {
    private static Animation INSTANCE = new Animation();
    public Setting<Boolean> playersDisableAnimations  = register(new Setting<>("DisableAnimations", false));

    public Setting<Mode> mode = this.register(new Setting("OldAnimations", Mode.NoDelay));
    public Setting<WhatHand> whatHand = this.register(new Setting<>("Swing", WhatHand.Mainhand));
    public Setting<Boolean> swing = register(new Setting<>("customSwingAnimation", false));
    public Setting<Integer> speed = register(new Setting<>("Speed", 13, 1, 20,v -> this.swing.getValue()));

    public Animation() {
        super("Animation", "Change animations.", ModuleCategory.RENDER);
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
        if (playersDisableAnimations.getValue()) {
            for (EntityPlayer player : Animation.mc.world.playerEntities) {
                player.limbSwing = Float.intBitsToFloat(Float.floatToIntBits(1.8755627E38f) ^ 0x7F0D1A06);
                player.limbSwingAmount = Float.intBitsToFloat(Float.floatToIntBits(6.103741E37f) ^ 0x7E37AD83);
                player.prevLimbSwingAmount = Float.intBitsToFloat(Float.floatToIntBits(4.8253957E37f) ^ 0x7E11357F);
            }
        }
        if (this.whatHand.getValue() == WhatHand.Offhand) {
            Animation.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (this.mode.getValue() == Mode.NoDelay && Animation.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            Animation.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            Animation.mc.entityRenderer.itemRenderer.itemStackMainHand = Animation.mc.player.getHeldItemMainhand();
        }
        if (this.whatHand.getValue().equals((Object) WhatHand.PacketSwing) && Animation.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && (double) Animation.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            Animation.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            Animation.mc.entityRenderer.itemRenderer.itemStackMainHand = Animation.mc.player.getHeldItemMainhand();
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

