package com.aljun.uninfectedzone.core.client;

import com.aljun.uninfectedzone.core.config.ConfigType;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.file.config.ConfigFileUtils;
import com.google.gson.JsonObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ClientConfigUtils {

    public static void receive(JsonObject jsonObject) {
        if (jsonObject.get("type").isJsonNull()) return;
        String name = jsonObject.get("type").getAsString();
        Optional<ConfigType> type = Arrays.stream(ConfigType.values()).filter((type1) -> type1.getName().equals(name)).findFirst();
        type.ifPresent(configType -> UninfectedZoneConfig.loadAndFixJson(jsonObject, configType));
    }

    public static void reloadGlobal() {
        JsonObject client = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.CLIENT);
        if (client != null) {
            UninfectedZoneConfig.loadAndFixJson(client, ConfigType.CLIENT);
            ConfigFileUtils.saveConfig(ConfigType.CLIENT, client);
        }
    }
}
