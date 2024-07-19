package com.aljun.uninfectedzone.core.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;


public class ComponentUtils {
    public static Component literature(String string) {
        return Component.nullToEmpty(string);
    }

    public static Component translate(String string) {
        return new TranslatableComponent(string);
    }

    public static Component translate(String string, Object... args) {
        return new TranslatableComponent(string, args);
    }
}
