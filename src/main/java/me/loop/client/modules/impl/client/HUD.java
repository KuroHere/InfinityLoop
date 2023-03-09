package me.loop.client.modules.impl.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.InfinityLoop;
import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.events.impl.render.Render2DEvent;
import me.loop.api.managers.Managers;
import me.loop.api.managers.impl.TextManager;
import me.loop.api.utils.impl.EntityUtil;
import me.loop.api.utils.impl.maths.MathUtil;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.api.utils.impl.worlds.Timer;
import me.loop.client.Client;
import me.loop.client.modules.Category;
import me.loop.client.modules.Module;
import me.loop.client.modules.impl.client.ClickGui.ClickGui;
import me.loop.client.modules.impl.misc.ToolTips;
import me.loop.client.modules.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HUD extends Module {
    private static final ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
    private static final ResourceLocation codHitmarker = new ResourceLocation("Loop", "sounds/cod_hitmarker");
    private static final ResourceLocation csgoHitmarker = new ResourceLocation("Loop", "sounds/csgo_hitmarker");
    private static HUD INSTANCE = new HUD();
    public Setting<Integer> moduleListUpdates = this.add(new Setting<Integer>("ALUpdates", 1000, 0, 1000));
    public Setting<Integer> textRadarUpdates = this.add(new Setting<Integer>("TRUpdates", 500, 0, 1000));
    private final Setting<Boolean> renderingUp = this.add(new Setting<Boolean>("RenderingUp", Boolean.valueOf(false), "Orientation of the HUD-Elements."));
    private final Setting<WaterMark> watermark = this.add(new Setting<WaterMark>("Logo", WaterMark.NONE, "WaterMark"));
    private final Setting<Boolean> modeVer = this.add(new Setting<Object>("Version", Boolean.valueOf(false), v -> this.watermark.getValue() != WaterMark.NONE));
    private final Setting<Boolean> arrayList = this.add(new Setting<Boolean>("ActiveModules", Boolean.valueOf(false), "Lists the active modules."));
    private final Setting<Boolean> moduleColors = this.add(new Setting<Object>("ModuleColors", Boolean.valueOf(false), v -> this.arrayList.getValue()));
    private final Setting<Boolean> alphabeticalSorting = this.add(new Setting<Object>("AlphabeticalSorting", Boolean.valueOf(false), v -> this.arrayList.getValue()));
    private final Setting<Boolean> serverBrand = this.add(new Setting<Boolean>("ServerBrand", Boolean.valueOf(false), "Brand of the server you are on."));
    private final Setting<Boolean> ping = this.add(new Setting<Boolean>("Ping", Boolean.valueOf(false), "Your response time to the server."));
    private final Setting<Boolean> tps = this.add(new Setting<Boolean>("TPS", Boolean.valueOf(false), "Ticks per second of the server."));
    private final Setting<Boolean> fps = this.add(new Setting<Boolean>("FPS", Boolean.valueOf(false), "Your frames per second."));
    private final Setting<LagNotify> lag = this.add(new Setting<LagNotify>("Lag", LagNotify.GRAY, "Lag Notifier"));
    public Setting<Integer> lagTime = add(new Setting("LagTime", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(2000)));
    private final Setting<Boolean> coords = this.add(new Setting<Boolean>("Coords", Boolean.valueOf(false), "Your current coordinates"));
    private final Setting<Boolean> direction = this.add(new Setting<Boolean>("Direction", Boolean.valueOf(false), "The Direction you are facing."));
    private final Setting<Boolean> speed = this.add(new Setting<Boolean>("Speed", Boolean.valueOf(false), "Your Speed"));
    private final Setting<Boolean> potions = this.add(new Setting<Boolean>("Potions", Boolean.valueOf(false), "Active potion effects"));
    private final Setting<Boolean> altPotionsColors = this.add(new Setting<Object>("AltPotionColors", Boolean.valueOf(false), v -> this.potions.getValue()));
    private final Setting<Boolean> armor = this.add(new Setting<Boolean>("Armor", Boolean.valueOf(false), "ArmorHUD"));
    private final Setting<Boolean> durability = this.add(new Setting<Boolean>("Durability", Boolean.valueOf(false), "Durability"));
    private final Setting<Boolean> percent = this.add(new Setting<Object>("Percent", Boolean.valueOf(true), v -> this.armor.getValue()));
    private final Setting<Boolean> totems = this.add(new Setting<Boolean>("Totems", Boolean.valueOf(false), "TotemHUD"));
    private final Setting<Greeter> greeter = this.add(new Setting<Greeter>("Greeter", Greeter.NONE, "Greets you."));
    private final Setting<String> spoofGreeter = this.add(new Setting<Object>("GreeterName", "InfinityLoop", v -> this.greeter.getValue() == Greeter.CUSTOM));
    private final Setting<Boolean> hitMarkers = this.add(new Setting<Boolean>("HitMarkers", false));
    private final Setting<Boolean> grayNess = this.add(new Setting<Boolean>("FutureColour", false));
    public Setting<String> command = add(new Setting("Command", "OyVey"));

    private final Timer timer = new Timer();
    private final Timer moduleTimer = new Timer();
    private final Map<Potion, Color> potionColorMap = new HashMap<Potion, Color>();
    public Setting<Boolean> colorSync = this.add(new Setting<Boolean>("Sync", Boolean.valueOf(false), "Universal colors for hud."));
    public Setting<Boolean> rainbow = this.add(new Setting<Boolean>("Rainbow", Boolean.valueOf(false), "Rainbow hud."));
    public Setting<Integer> factor = this.add(new Setting<Object>("Factor", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(20), v -> this.rainbow.getValue()));
    public Setting<Boolean> rolling = this.add(new Setting<Object>("Rolling", Boolean.valueOf(false), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowSpeed = this.add(new Setting<Object>("RSpeed", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowSaturation = this.add(new Setting<Object>("Saturation", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowBrightness = this.add(new Setting<Object>("Brightness", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));
    public Setting<Boolean> potionIcons = this.add(new Setting<Boolean>("PotionIcons", Boolean.valueOf(true), "Draws Potion Icons."));
    public Setting<Boolean> shadow = this.add(new Setting<Boolean>("Shadow", Boolean.valueOf(false), "Draws the text with a shadow."));
    public Setting<Integer> animationHorizontalTime = this.add(new Setting<Object>("AnimationHTime", Integer.valueOf(500), Integer.valueOf(1), Integer.valueOf(1000), v -> this.arrayList.getValue()));
    public Setting<Integer> animationVerticalTime = this.add(new Setting<Object>("AnimationVTime", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(500), v -> this.arrayList.getValue()));
    public Setting<Boolean> textRadar = this.add(new Setting<Boolean>("TextRadar", Boolean.valueOf(false), "A TextRadar"));
    public Setting<Boolean> time = this.add(new Setting<Boolean>("Time", Boolean.valueOf(false), "The time"));
    public Setting<Integer> hudRed = this.add(new Setting<Object>("Red", Integer.valueOf(135), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> hudGreen = this.add(new Setting<Object>("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> hudBlue = this.add(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Boolean> potions1 = this.add(new Setting<Object>("LevelPotions", Boolean.valueOf(false), v -> this.potions.getValue()));
    public Setting<Boolean> MS = this.add(new Setting<Object>("ms", Boolean.valueOf(false), v -> this.ping.getValue()));
    public Map<Module, Float> moduleProgressMap = new HashMap<Module, Float>();
    public Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
    private Map<String, Integer> players = new HashMap<String, Integer>();
    private int color;
    private boolean shouldIncrement;
    private int hitMarkerTimer;


    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Category.CLIENT, true, false, false);
        this.setInstance();
        this.potionColorMap.put(MobEffects.SPEED, new Color(124, 175, 198));
        this.potionColorMap.put(MobEffects.SLOWNESS, new Color(90, 108, 129));
        this.potionColorMap.put(MobEffects.HASTE, new Color(217, 192, 67));
        this.potionColorMap.put(MobEffects.MINING_FATIGUE, new Color(74, 66, 23));
        this.potionColorMap.put(MobEffects.STRENGTH, new Color(147, 36, 35));
        this.potionColorMap.put(MobEffects.INSTANT_HEALTH, new Color(67, 10, 9));
        this.potionColorMap.put(MobEffects.INSTANT_DAMAGE, new Color(67, 10, 9));
        this.potionColorMap.put(MobEffects.JUMP_BOOST, new Color(34, 255, 76));
        this.potionColorMap.put(MobEffects.NAUSEA, new Color(85, 29, 74));
        this.potionColorMap.put(MobEffects.REGENERATION, new Color(205, 92, 171));
        this.potionColorMap.put(MobEffects.RESISTANCE, new Color(153, 69, 58));
        this.potionColorMap.put(MobEffects.FIRE_RESISTANCE, new Color(228, 154, 58));
        this.potionColorMap.put(MobEffects.WATER_BREATHING, new Color(46, 82, 153));
        this.potionColorMap.put(MobEffects.INVISIBILITY, new Color(127, 131, 146));
        this.potionColorMap.put(MobEffects.BLINDNESS, new Color(31, 31, 35));
        this.potionColorMap.put(MobEffects.NIGHT_VISION, new Color(31, 31, 161));
        this.potionColorMap.put(MobEffects.HUNGER, new Color(88, 118, 83));
        this.potionColorMap.put(MobEffects.WEAKNESS, new Color(72, 77, 72));
        this.potionColorMap.put(MobEffects.POISON, new Color(78, 147, 49));
        this.potionColorMap.put(MobEffects.WITHER, new Color(53, 42, 39));
        this.potionColorMap.put(MobEffects.HEALTH_BOOST, new Color(248, 125, 35));
        this.potionColorMap.put(MobEffects.ABSORPTION, new Color(37, 82, 165));
        this.potionColorMap.put(MobEffects.SATURATION, new Color(248, 36, 35));
        this.potionColorMap.put(MobEffects.GLOWING, new Color(148, 160, 97));
        this.potionColorMap.put(MobEffects.LEVITATION, new Color(206, 255, 255));
        this.potionColorMap.put(MobEffects.LUCK, new Color(51, 153, 0));
        this.potionColorMap.put(MobEffects.UNLUCK, new Color(192, 164, 77));
    }

    public static HUD getInstance() {
        if (HUD.INSTANCE == null) {
            HUD.INSTANCE = new HUD();
        }
        return HUD.INSTANCE;
    }

    private void setInstance() {
        HUD.INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        for (final Module module : Managers.moduleManager.sortedModules) {
            if (module.isDisabled() && module.arrayListOffset == 0.0f) {
                module.sliding = true;
            }
        }
        if (this.timer.passedMs(this.textRadarUpdates.getValue())) {
            this.players = this.getTextRadarPlayers();
            this.timer.reset();
        }
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }

    @SubscribeEvent
    public void onModuleToggle(final ClientEvent event) {
        if (event.getFeature() instanceof Module) {
            if (event.getStage() == 0) {
                for (float i = 0.0f; i <= this.renderer.getStringWidth(((Module) event.getFeature()).getDisplayName()); i += this.renderer.getStringWidth(((Module) event.getFeature()).getDisplayName()) / 500.0f) {
                    if (this.moduleTimer.passedMs(1L)) {
                        this.moduleProgressMap.put((Module) event.getFeature(), this.renderer.getStringWidth(((Module) event.getFeature()).getDisplayName()) - i);
                    }
                    this.timer.reset();
                }
            } else if (event.getStage() == 1) {
                for (float i = 0.0f; i <= this.renderer.getStringWidth(((Module) event.getFeature()).getDisplayName()); i += this.renderer.getStringWidth(((Module) event.getFeature()).getDisplayName()) / 500.0f) {
                    if (this.moduleTimer.passedMs(1L)) {
                        this.moduleProgressMap.put((Module) event.getFeature(), this.renderer.getStringWidth(((Module) event.getFeature()).getDisplayName()) - i);
                    }
                    this.timer.reset();
                }
            }
        }
    }

    @Override
    public void onRender2D(final Render2DEvent event) {
        if (Client.fullNullCheck()) {
            return;
        }
        final int colorSpeed = 101 - this.rainbowSpeed.getValue();
        final float hue = this.colorSync.getValue() ? Colors.getInstance().hue : (System.currentTimeMillis() % (360 * colorSpeed) / (360.0f * colorSpeed));
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        float tempHue = hue;
        for (int i = 0; i <= height; ++i) {
            if (this.colorSync.getValue()) {
                this.colorMap.put(i, Color.HSBtoRGB(tempHue, Colors.getInstance().rainbowSaturation.getValue() / 255.0f, Colors.getInstance().rainbowBrightness.getValue() / 255.0f));
            } else {
                this.colorMap.put(i, Color.HSBtoRGB(tempHue, this.rainbowSaturation.getValue() / 255.0f, this.rainbowBrightness.getValue() / 255.0f));
            }
            tempHue += 1.0f / height * this.factor.getValue();
        }
        GlStateManager.pushMatrix();
        if (this.rainbow.getValue() && !this.rolling.getValue()) {
            this.color = (this.colorSync.getValue() ? Colors.getInstance().getCurrentColor().getRGB() : Color.HSBtoRGB(hue, this.rainbowSaturation.getValue() / 255.0f, this.rainbowBrightness.getValue() / 255.0f));
        } else if (!this.rainbow.getValue()) {
            this.color = (this.colorSync.getValue() ? Colors.getInstance().getCurrentColor().getRGB() : ColorUtil.toRGBA(this.hudRed.getValue(), this.hudGreen.getValue(), this.hudBlue.getValue()));
        }
        final String grayString = this.grayNess.getValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        switch (this.watermark.getValue()) {
            case INFINITYLOOP: {
                this.renderer.drawString("InfinityLoop " + (this.modeVer.getValue() ? InfinityLoop.MODVER : ""), 2.0f, 2.0f, (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2) : this.color, true);
                break;
            }
        }
        if (this.textRadar.getValue()) {
            this.drawTextRadar((ToolTips.getInstance().isOff() || !ToolTips.getInstance().shulkerSpy.getValue() || !ToolTips.getInstance().render.getValue()) ? 0 : ToolTips.getInstance().getTextRadarY());
        }
        int[] counter1 = {1};
        int j = this.renderingUp.getValue() ? 0 : ((HUD.mc.currentScreen instanceof GuiChat) ? 14 : 0);
        if (this.arrayList.getValue()) {
            if (this.renderingUp.getValue()) {
                for (int k = 0; k < (this.alphabeticalSorting.getValue() ? Managers.moduleManager.alphabeticallySortedModules.size() : Managers.moduleManager.sortedModules.size()); ++k) {
                    final Module module = this.alphabeticalSorting.getValue() ? Managers.moduleManager.alphabeticallySortedModules.get(k) : Managers.moduleManager.sortedModules.get(k);
                    final String text = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    this.renderer.drawString(text, (width - 2 - this.renderer.getStringWidth(text)), (2 + j * 10), ClickGui.getInstance().colorSync.getValue() && (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    j++;
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                for (int k = 0; k < (this.alphabeticalSorting.getValue() ? Managers.moduleManager.alphabeticallySortedModules.size() : Managers.moduleManager.sortedModules.size()); ++k) {
                    final Module module = this.alphabeticalSorting.getValue() ? Managers.moduleManager.alphabeticallySortedModules.get(Managers.moduleManager.alphabeticallySortedModules.size() - 1 - k) : Managers.moduleManager.sortedModules.get(k);
                    final String text = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    final TextManager renderer = this.renderer;
                    final String text5 = text;
                    final float x = width - 2 - this.renderer.getStringWidth(text) + ((this.animationHorizontalTime.getValue() == 1) ? 0.0f : module.arrayListOffset);
                    final int n = height;
                    j += 10;
                    this.renderer.drawString(text5, x, (float) (n - j), ClickGui.getInstance().colorSync.getValue() && (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        }
        int k = this.renderingUp.getValue() ? ((HUD.mc.currentScreen instanceof GuiChat) ? 0 : 0) : 0;
        if (this.renderingUp.getValue()) {
            if (this.serverBrand.getValue()) {
                final String text2 = grayString + "Server brand " + ChatFormatting.WHITE + Managers.serverManager.getServerBrand();
                final TextManager renderer2 = this.renderer;
                final String text6 = text2;
                final float x2 = (float) (width - (this.renderer.getStringWidth(text2) + 2));
                final int n2 = height - 2;
                k += 10;
                renderer2.drawString(text6, x2, (float) (n2 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(height - k) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.potions.getValue()) {
                for (final PotionEffect effect : Managers.potionManager.getOwnPotions()) {
                    final String text3 = this.altPotionsColors.getValue() ? Managers.potionManager.getPotionString(effect) : Managers.potionManager.getColoredPotionString(effect);
                    final TextManager renderer3 = this.renderer;
                    final String text7 = text3;
                    final float x3 = (float) (width - (this.renderer.getStringWidth(text3) + 2));
                    final int n3 = height - 2;
                    k += 10;
                    renderer3.drawString(text7, x3, (float) (n3 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(height - k) : (this.altPotionsColors.getValue() ? this.potionColorMap.get(effect.getPotion()).getRGB() : this.color), true);
                    counter1[0] = counter1[0] + 1;
                }
            }
            if (this.speed.getValue()) {
                final String text2 = grayString + "Speed " + ChatFormatting.WHITE + Managers.speedManager.getSpeedKpH() + " km/h";
                final TextManager renderer4 = this.renderer;
                final String text8 = text2;
                final float x4 = (float) (width - (this.renderer.getStringWidth(text2) + 2));
                final int n4 = height - 2;
                k += 10;
                renderer4.drawString(text8, x4, (float) (n4 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(height - k) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.time.getValue()) {
                final String text2 = grayString + "Time " + ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                final TextManager renderer5 = this.renderer;
                final String text9 = text2;
                final float x5 = (float) (width - (this.renderer.getStringWidth(text2) + 2));
                final int n5 = height - 2;
                k += 10;
                renderer5.drawString(text9, x5, (float) (n5 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(height - k) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.durability.getValue()) {
                final int itemDamage = HUD.mc.player.getHeldItemMainhand().getMaxDamage() - HUD.mc.player.getHeldItemMainhand().getItemDamage();
                if (itemDamage > 0) {
                    final String text = grayString + "Durability " + ChatFormatting.RESET + itemDamage;
                    final TextManager renderer6 = this.renderer;
                    final String text10 = text;
                    final float x6 = (float) (width - (this.renderer.getStringWidth(text) + 2));
                    final int n6 = height - 2;
                    k += 10;
                    renderer6.drawString(text10, x6, (float) (n6 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(height - k) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
            if (this.tps.getValue()) {
                final String text2 = grayString + "TPS " + ChatFormatting.WHITE + Managers.serverManager.getTPS();
                final TextManager renderer7 = this.renderer;
                final String text11 = text2;
                final float x7 = (float) (width - (this.renderer.getStringWidth(text2) + 2));
                final int n7 = height - 2;
                k += 10;
                renderer7.drawString(text11, x7, (float) (n7 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(height - k) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            final String text = grayString + "Ping " + (this.MS.getValue() ? "ms" : "");
            if (this.renderer.getStringWidth(text) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    final TextManager renderer8 = this.renderer;
                    final String text12 = text;
                    final float x8 = (float) (width - (this.renderer.getStringWidth(text) + 2));
                    final int n8 = height - 2;
                    k += 10;
                    renderer8.drawString(text12, x8, (float) (n8 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(height - k) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.fps.getValue()) {
                    final TextManager renderer9 = this.renderer;
                    final String text13 = fpsText;
                    final float x9 = (float) (width - (this.renderer.getStringWidth(fpsText) + 2));
                    final int n9 = height - 2;
                    k += 10;
                    renderer9.drawString(text13, x9, (float) (n9 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(height - k) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (this.fps.getValue()) {
                    final TextManager renderer10 = this.renderer;
                    final String text14 = fpsText;
                    final float x10 = (float) (width - (this.renderer.getStringWidth(fpsText) + 2));
                    final int n10 = height - 2;
                    k += 10;
                    renderer10.drawString(text14, x10, (float) (n10 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(height - k) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.ping.getValue()) {
                    final TextManager renderer11 = this.renderer;
                    final String text15 = text;
                    final float x11 = (float) (width - (this.renderer.getStringWidth(text) + 2));
                    final int n11 = height - 2;
                    k += 10;
                    renderer11.drawString(text15, x11, (float) (n11 - k), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(height - k) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            if (this.serverBrand.getValue()) {
                final String text2 = grayString + "Server brand " + ChatFormatting.WHITE + Managers.serverManager.getServerBrand();
                this.renderer.drawString(text2, (float) (width - (this.renderer.getStringWidth(text2) + 2)), (float) (2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2 + k * 10) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.potions.getValue()) {
                for (final PotionEffect effect : Managers.potionManager.getOwnPotions()) {
                    final String text3 = this.altPotionsColors.getValue() ? Managers.potionManager.getPotionString(effect) : Managers.potionManager.getColoredPotionString(effect);
                    this.renderer.drawString(text3, (float) (width - (this.renderer.getStringWidth(text3) + 2)), (float) (2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2 + k * 10) : (this.altPotionsColors.getValue() ? this.potionColorMap.get(effect.getPotion()).getRGB() : this.color), true);
                    counter1[0] = counter1[0] + 1;
                }
            }
            if (this.speed.getValue()) {
                final String text2 = grayString + "Speed " + ChatFormatting.WHITE + Managers.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(text2, (float) (width - (this.renderer.getStringWidth(text2) + 2)), (float) (2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2 + k * 10) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.time.getValue()) {
                final String text2 = grayString + "Time " + ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                this.renderer.drawString(text2, (float) (width - (this.renderer.getStringWidth(text2) + 2)), (float) (2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2 + k * 10) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.durability.getValue()) {
                final int itemDamage = HUD.mc.player.getHeldItemMainhand().getMaxDamage() - HUD.mc.player.getHeldItemMainhand().getItemDamage();
                if (itemDamage > 0) {
                    final String text = grayString + "Durability " + ChatFormatting.GREEN + itemDamage;
                    this.renderer.drawString(text, (float) (width - (this.renderer.getStringWidth(text) + 2)), (float) (2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2 + k * 10) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
            if (this.tps.getValue()) {
                final String text2 = grayString + "TPS " + ChatFormatting.WHITE + Managers.serverManager.getTPS();
                this.renderer.drawString(text2, (float) (width - (this.renderer.getStringWidth(text2) + 2)), (float) (2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2 + k * 10) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            final String text = grayString + "Ping " + ChatFormatting.WHITE + Managers.serverManager.getPing();
            if (this.renderer.getStringWidth(text) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    this.renderer.drawString(text, (float) (width - (this.renderer.getStringWidth(text) + 2)), (float) (2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2 + k * 10) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float) (width - (this.renderer.getStringWidth(fpsText) + 2)), (float) (2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2 + k * 10) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float) (width - (this.renderer.getStringWidth(fpsText) + 2)), (float) (2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2 + k * 10) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.ping.getValue()) {
                    this.renderer.drawString(text, (float) (width - (this.renderer.getStringWidth(text) + 2)), (float) (2 + k++ * 10), (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2 + k * 10) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        }
        final boolean inHell = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName().equals("Hell");
        final int posX = (int) HUD.mc.player.posX;
        final int posY = (int) HUD.mc.player.posY;
        final int posZ = (int) HUD.mc.player.posZ;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int) (HUD.mc.player.posX * nether);
        final int hposZ = (int) (HUD.mc.player.posZ * nether);
        if (this.renderingUp.getValue()) {
            Managers.notificationManager.handleNotifications(height - (k + 16));
        } else {
            Managers.notificationManager.handleNotifications(height - (j + 16));
        }
        k = ((HUD.mc.currentScreen instanceof GuiChat) ? 14 : 0);
        final String coordinates = String.valueOf(ChatFormatting.WHITE) + posX + ChatFormatting.GRAY + " [" + hposX + "], " + ChatFormatting.WHITE + posY + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + posZ + ChatFormatting.GRAY + " [" + hposZ + "]";
        final String text4 = (this.direction.getValue() ? (Managers.rotationManager.getDirection4D(false) + " ") : "") + (this.coords.getValue() ? coordinates : "") + "";
        final TextManager renderer12 = this.renderer;
        final String text16 = text4;
        final float x12 = 2.0f;
        final int n12 = height;
        k += 10;
        final float y = (float) (n12 - k);
        int color;
        if (this.rolling.getValue() && this.rainbow.getValue()) {
            final Map<Integer, Integer> colorMap = this.colorMap;
            final int n13 = height;
            k += 10;
            color = colorMap.get(n13 - k);
        } else {
            counter1[0] = counter1[0] + 1;
            color = this.color;
        }
        renderer12.drawString(text16, x12, y, color, true);
        if (this.armor.getValue()) {
            this.renderArmorHUD(this.percent.getValue());
        }
        if (this.totems.getValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue() != Greeter.NONE) {
            this.renderGreeter();
        }
        if (this.lag.getValue() != LagNotify.NONE) {
            this.renderLag();
        }
        if (this.hitMarkers.getValue() && this.hitMarkerTimer > 0) {
            this.drawHitMarkers();
        }
        GlStateManager.popMatrix();
    }

    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }

    public void renderGreeter() {
        final int width = this.renderer.scaledWidth;
        String text = "";
        int[] counter1 = {1};
        switch (this.greeter.getValue()) {
            case TIME: {
                text = text + MathUtil.getTimeOfDay() + HUD.mc.player.getDisplayNameString();
                break;
            }
            case CHRISTMAS: {
                text = text + "Merry Christmas " + HUD.mc.player.getDisplayNameString() + " :^)";
                break;
            }
            case LONG: {
                text = text + "Welcome to InfinityLoop " + HUD.mc.player.getDisplayNameString() + " :^)";
                break;
            }
            case CUSTOM: {
                text += this.spoofGreeter.getValue();
                break;
            }
            default: {
                text = text + "Welcome " + HUD.mc.player.getDisplayNameString();
                break;
            }
        }
        this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(2) : this.color, true);
        counter1[0] = counter1[0] + 1;
    }

    public void renderLag() {
        final int width = this.renderer.scaledWidth;
        if (Managers.serverManager.isServerNotResponding()) {
            final String text = ((this.lag.getValue() == LagNotify.GRAY) ? ChatFormatting.GRAY : ChatFormatting.RED) + "Server not responding: " + MathUtil.round(Managers.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 20.0f, (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(20) : this.color, true);
        }
    }

    public void renderArrayList() {
    }

    public void renderTotemHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int totems = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            final int iteration = 0;
            final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.totem, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.totem, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(totems + "", (float) (x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (float) (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }

    public void renderArmorHUD(final boolean percent) {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        GlStateManager.enableTexture2D();
        final int i = width / 2;
        int iteration = 0;
        final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
        for (final ItemStack is : HUD.mc.player.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, is, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
            this.renderer.drawStringWithShadow(s, (float) (x + 19 - 2 - this.renderer.getStringWidth(s)), (float) (y + 9), 16777215);
            if (!percent) {
                continue;
            }
            int dmg = 0;
            final int itemDurability = is.getMaxDamage() - is.getItemDamage();
            final float green = (is.getMaxDamage() - (float) is.getItemDamage()) / is.getMaxDamage();
            final float red = 1.0f - green;
            if (percent) {
                dmg = 100 - (int) (red * 100.0f);
            } else {
                dmg = itemDurability;
            }
            this.renderer.drawStringWithShadow(dmg + "", (float) (x + 8 - this.renderer.getStringWidth(dmg + "") / 2), (float) (y - 11), ColorUtil.toRGBA((int) (red * 255.0f), (int) (green * 255.0f), 0));
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    public void drawHitMarkers() {
        final ScaledResolution resolution = new ScaledResolution(HUD.mc);
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
    }

    public void drawTextRadar(final int yOffset) {
        if (!this.players.isEmpty()) {
            int y = this.renderer.getFontHeight() + 7 + yOffset;
            for (final Map.Entry<String, Integer> player : this.players.entrySet()) {
                final String text = player.getKey() + " ";
                final int textheight = this.renderer.getFontHeight() + 1;
                this.renderer.drawString(text, 2.0f, (float) y, (this.rolling.getValue() && this.rainbow.getValue()) ? this.colorMap.get(y) : this.color, true);
                y += textheight;
            }
        }
    }

    public enum Greeter {
        NONE,
        NAME,
        TIME,
        CHRISTMAS,
        LONG,
        CUSTOM
    }

    public enum LagNotify {
        NONE,
        RED,
        GRAY
    }

    public enum WaterMark {
        NONE,
        INFINITYLOOP
    }

    public enum Sound {
        NONE,
        COD,
        CSGO
    }
}