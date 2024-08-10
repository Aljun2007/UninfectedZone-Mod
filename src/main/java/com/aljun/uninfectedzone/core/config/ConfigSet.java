package com.aljun.uninfectedzone.core.config;

import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.VarSet;
import net.minecraft.network.chat.Component;

import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigSet<T> {

    public final VarSet<T> VAR_SET;
    public final ConfigType CONFIG_TYPE;
    private String description = null;
    private Supplier<Boolean> active = () -> true;
    private Function<T, Component> valueDisplay = t -> ComponentUtils.literature(t.toString());

    ConfigSet(VarSet<T> varSet, ConfigType configType) {
        VAR_SET = varSet;
        CONFIG_TYPE = configType;
    }

    void setValueDisplay(Function<T, Component> valueDisplay) {
        this.valueDisplay = valueDisplay;
    }

    public boolean isActive() {
        return active.get();
    }

    void setActive(Supplier<Boolean> active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        return VAR_SET.hashCode();
    }

    public Component getDescription() {
        return ComponentUtils.translate(description);
    }

    void setDescription(String description) {
        this.description = description;
    }


    public Component getValueDisplay(T value) {
        return this.valueDisplay.apply(value);
    }
}
