package com.aljun.uninfectedzone.core.file.config;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.config.ConfigType;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.file.FileUtils;
import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerLevel;

public class ConfigFileLoader {
    public static String getClientPath() {
        return UninfectedZone.getAbsPath() + "\\config\\client.json";
    }

    public static String getGameRulePath(ServerLevel level) {
        return level.getServer().getServerDirectory().getAbsolutePath();
    }

    public static void loadGlobalConfigJson() {
        JsonObject object = FileUtils.loadJsonFileOrCreate(getGlobalPath());
        UninfectedZoneConfig.loadAndFixJson(object, ConfigType.GLOBAL);
        FileUtils.saveJsonFile(getGlobalPath(), object);
    }

    public static String getGlobalPath() {
        return UninfectedZone.getAbsPath() + "\\config\\global.json";
    }
}
