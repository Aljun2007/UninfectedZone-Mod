package com.aljun.uninfectedzone.api.config;

import com.aljun.uninfectedzone.core.config.ConfigSet;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;

@SuppressWarnings("unused")
public class UninfectedZoneConfigAPI {
    public static <T> T get(ConfigSet<T> configSet) {
        return UninfectedZoneConfig.get(configSet);
    }

    public static <T> boolean setOrDefault(ConfigSet<T> configSet, T t) {
        return UninfectedZoneConfig.setOrDefault(configSet, t);
    }

    public static <T> boolean setOrOrigin(ConfigSet<T> configSet, T t) {
        return UninfectedZoneConfig.setOrOrigin(configSet, t);
    }
}
