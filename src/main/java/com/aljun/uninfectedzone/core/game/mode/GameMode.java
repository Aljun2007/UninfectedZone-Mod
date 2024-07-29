package com.aljun.uninfectedzone.core.game.mode;

import com.google.gson.JsonObject;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class GameMode extends ForgeRegistryEntry<GameMode> {
    public static String RELATIVE_PATH = "\\game_mode.json";

    protected abstract void load(JsonObject dataJson);

    protected abstract void save(JsonObject dataJson);
}
