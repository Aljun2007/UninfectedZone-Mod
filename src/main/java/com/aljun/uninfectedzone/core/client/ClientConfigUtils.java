package com.aljun.uninfectedzone.core.client;

import com.aljun.uninfectedzone.core.config.ConfigType;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.file.config.ConfigFileUtils;
import com.aljun.uninfectedzone.core.network.ConfigEnquiringNetworking;
import com.aljun.uninfectedzone.core.network.ConfigJsonNetworking;
import com.google.gson.JsonObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ClientConfigUtils {

    public static void receive(JsonObject jsonObject) {
        if (jsonObject.get("type").isJsonNull()) return;
        String name = jsonObject.get("type").getAsString();
        Optional<ConfigType> type = Arrays.stream(ConfigType.values()).filter(type1 -> type1.getName().equals(name)).findFirst();
        type.ifPresent(configType -> UninfectedZoneConfig.loadOrAbsent(jsonObject, configType));
    }

    public static void loadGlobalClient() {
        JsonObject client = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.CLIENT);
        if (client != null) {
            UninfectedZoneConfig.loadAndFixJson(client, ConfigType.CLIENT);
            ConfigFileUtils.saveConfig(ConfigType.CLIENT, client);
        }
    }

    public static void saveGlobalClient() {
        JsonObject client = UninfectedZoneConfig.toJsonObject(ConfigType.CLIENT);
        UninfectedZoneConfig.writeToJson(client, ConfigType.CLIENT);
        ConfigFileUtils.saveConfig(ConfigType.CLIENT, client);
    }

    public static void sendToServer(JsonObject jsonObject) {
        ConfigJsonNetworking.INSTANCE.send(
                PacketDistributor.SERVER.noArg(),
                ConfigJsonNetworking.createPack(jsonObject)
        );
    }

    public static void enquireSever() {
        ConfigEnquiringNetworking.INSTANCE.send(PacketDistributor.SERVER.noArg(), ConfigEnquiringNetworking.createPack());
    }
}
