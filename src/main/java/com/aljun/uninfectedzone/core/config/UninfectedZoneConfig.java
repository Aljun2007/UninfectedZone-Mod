package com.aljun.uninfectedzone.core.config;

import com.aljun.uninfectedzone.core.utils.JsonManager;
import com.aljun.uninfectedzone.core.utils.VarSet;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;


public class UninfectedZoneConfig {
    private static final HashMap<ConfigType, HashMap<String, ConfigHolder<?>>> CONFIGS_HOLDERS = new HashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean build = false;

    static {
        for (ConfigType type : ConfigType.values()) {
            CONFIGS_HOLDERS.put(type, new HashMap<>());
        }
    }

    public static void loadAndFixJson(JsonObject jsonObject, ConfigType configType) {
        HashMap<String, ConfigHolder<?>> config = CONFIGS_HOLDERS.get(configType);
        JsonManager jsonManager = new JsonManager(jsonObject);
        config.forEach(((s, configHolder) -> {
            configHolder.loadFromJson(jsonManager);
        }));
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(@NotNull ConfigSet<T> configSet) {
        return (T) CONFIGS_HOLDERS.get(configSet.CONFIG_TYPE).get(configSet.VAR_SET.ID).get();
    }

    public static <T> @Nullable ConfigSet<T> register(VarSet<T> varSet, ConfigType configType) {
        if (!build) {
            ConfigSet<T> configSet = new ConfigSet<>(varSet, configType);
            CONFIGS_HOLDERS.get(configType).put(varSet.ID, new ConfigHolder<>(configSet));
            return configSet;
        } else {
            LOGGER.error("has already stopped registering");
            return null;
        }
    }

    public static void stopRegister() {
        build = true;
    }
}
