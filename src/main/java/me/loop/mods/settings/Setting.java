package me.loop.mods.settings;

import me.loop.mods.Mod;
import me.loop.api.events.impl.client.ClientEvent;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.util.function.Predicate;

public class Setting<T> {

    private final String name;
    private Predicate<T> visibility;
    private Mod mod;
    private final T defaultValue;
    private T value, plannedValue, min, max;
    public boolean isOpen, restriction, shouldRenderStringName, booleanValue, hideAlpha;
    private String description;

    public Setting(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.plannedValue = defaultValue;
        this.description = "";
    }

    public Setting(String name, T defaultValue, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.plannedValue = defaultValue;
        this.description = description;
    }

    public Setting(String name, T defaultValue, T min, T max, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.description = description;

        this. restriction = true;
    }

    public Setting(String name, T defaultValue, T min, T max) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.description = "";

        this. restriction = true;
    }

    public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.visibility = visibility;
        this.description = description;

        this. restriction = true;
    }

    public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.visibility = visibility;
        this.description = "";

        this. restriction = true;
    }

    public Setting(String name, T defaultValue, Predicate<T> visibility) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.visibility = visibility;
        this.plannedValue = defaultValue;
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public T getPlannedValue() {
        return this.plannedValue;
    }

    public T getMin() {
        return this.min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return this.max;
    }

    public void setMax(T max) {
        this.max = max;
    }

    public Mod getMod() {
        return this.mod;
    }

    public String currentEnumName() {
        return EnumConverter.getProperName((Enum) this.value);
    }

    public String getType() {
        if (this.isEnumSetting()) {
            return "Enum";
        }
        return this.getClassName(this.defaultValue);
    }

    public <T> String getClassName(T value) {
        return value.getClass().getSimpleName();
    }

    public String getDescription() {
        if (this.description == null) {
            return "";
        }
        return this.description;
    }

    public boolean isNumberSetting() {
        return this.value instanceof Double
                || this.value instanceof Integer
                || this.value instanceof Short
                || this.value instanceof Long
                || this.value instanceof Float;
    }

    public boolean isEnumSetting() {
        return !this.isNumberSetting()
                && !(this.value instanceof String)
                && !(this.value instanceof Bind)
                && !(this.value instanceof Character)
                && !(this.value instanceof Boolean)
                && !(this.value instanceof Color);
    }

    public boolean isStringSetting() {
        return this.value instanceof String;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public boolean restriction() {
        return this. restriction;
    }

    public Setting<T> setRenderName(boolean renderName) {
        this.shouldRenderStringName = renderName;
        return this;
    }

    public boolean isVisible() {
        if (this.visibility == null) {
            return true;
        }
        return this.visibility.test(this.getValue());
    }

    //General setters

    public void setValue(T valueIn) {
        setPlannedValue(valueIn);

        if (restriction) {

            if (((Number) min).floatValue() > ((Number) valueIn).floatValue()) {
                setPlannedValue(min);
            }

            if (((Number) max).floatValue() < ((Number) valueIn).floatValue()) {
                setPlannedValue(max);
            }
        }

        ClientEvent event = new ClientEvent(this);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            value = plannedValue;

        } else {
            plannedValue = value;
        }
    }

    public void setPlannedValue(T valueIn) {
        plannedValue = valueIn;
    }

    public void setMod(Mod modIn) {
        mod = modIn;
    }

    //Enum setters

    public void setEnumValue(String value) {
        for (Enum e : ((Enum) this.value).getClass().getEnumConstants()) {

            if (!e.name().equalsIgnoreCase(value)) continue;

            this.value = (T) e;
        }
    }

    public void increaseEnum() {
        plannedValue = (T) EnumConverter.increaseEnum((Enum) value);

        ClientEvent event = new ClientEvent(this);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            value = plannedValue;

        } else {
            plannedValue = value;
        }
    }

    //Color picker setters

    /**
     * injectBoolean() is for adding a boolean value to a color picker (i.e. to add an option to enable/disable a color).
     * @param valueIn is the boolean value that'll be set by default.
     */

    public Setting<T> injectBoolean(boolean valueIn) {
        if (value instanceof Color) {
             restriction = true;
            booleanValue = valueIn;
        }

        return this;
    }

    /**
     * hideAlpha() is for disabling the alpha slider in color pickers which don't need it.
     */

    public Setting<T> hideAlpha() {
        hideAlpha = true;

        return this;
    }

    public static boolean nullCheck() {
        return Mod.mc.player == null;
    }
}

