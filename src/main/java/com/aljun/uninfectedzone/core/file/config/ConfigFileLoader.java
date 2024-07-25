package com.aljun.uninfectedzone.core.file.config;

import com.aljun.uninfectedzone.UninfectedZone;
import net.minecraft.server.level.ServerLevel;

public class ConfigFileLoader {
    public static String getGlobalPath() {
        return UninfectedZone.getAbsPath() + "//config//global.json";
    }

    public static String getClientPath() {
        return UninfectedZone.getAbsPath() + "//config//client.json";
    }

    public static String getGameRulePath(ServerLevel level) {
        return level.getServer().getServerDirectory().getAbsolutePath();
    }
}
