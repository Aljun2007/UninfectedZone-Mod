package com.aljun.uninfectedzone.core.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;


public class ComponentUtils {

    public static Component literature(String string) {
        return string == null ? EMPTY : Component.nullToEmpty(string);
    }

    public static TranslatableComponent translate(String string) {
        return new TranslatableComponent(string);
    }

    public static TranslatableComponent translate(String string, Object... args) {
        return new TranslatableComponent(string, args);
    }

    public static MutableComponent createModIDComponent(String modId) {
        return new TextComponent(modId).withStyle(ChatFormatting.DARK_BLUE);
    }    public static final Component EMPTY = literature("");




}
