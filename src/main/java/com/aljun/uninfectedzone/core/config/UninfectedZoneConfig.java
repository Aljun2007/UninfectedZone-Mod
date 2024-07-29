package com.aljun.uninfectedzone.core.config;

import com.aljun.uninfectedzone.core.file.config.ConfigFileUtils;
import com.aljun.uninfectedzone.core.network.ConfigServerToClientNetworking;
import com.aljun.uninfectedzone.core.utils.JsonManager;
import com.aljun.uninfectedzone.core.utils.LogicalUtils;
import com.aljun.uninfectedzone.core.utils.VarSet;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;


public class UninfectedZoneConfig {
    public static final VarSet<String> TYPE = VarSet.builder("description", VarSet.VarType.STRING).defaultVar("unknown").create("type");
    private static final HashMap<ConfigType, HashMap<String, ConfigHolder<?>>> CONFIGS_HOLDERS = new HashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean build = false;

    static {
        for (ConfigType type : ConfigType.values()) {
            CONFIGS_HOLDERS.put(type, new HashMap<>());
        }
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

    public static void reloadAll(MinecraftServer server) {
        reloadWorld(server);
        reloadGlobalServer(server);
    }

    public static void reloadWorld(MinecraftServer server) {
        if (LogicalUtils.isServer()) {
            JsonObject gameRule = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.GAME_RULE);
            if (gameRule != null) {
                UninfectedZoneConfig.loadAndFixJson(gameRule, ConfigType.GAME_RULE);
                ConfigFileUtils.saveConfig(ConfigType.GAME_RULE, gameRule);
            }

            JsonObject gameProperty = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.GAME_PROPERTY);
            if (gameProperty != null) {
                UninfectedZoneConfig.loadAndFixJson(gameProperty, ConfigType.GAME_PROPERTY);
                ConfigFileUtils.saveConfig(ConfigType.GAME_PROPERTY, gameProperty);
            }

            JsonObject gameData = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.GAME_DATA);
            if (gameData != null) {
                UninfectedZoneConfig.loadAndFixJson(gameData, ConfigType.GAME_DATA);
                ConfigFileUtils.saveConfig(ConfigType.GAME_DATA, gameData);
            }

            server.getPlayerList().getPlayers().forEach((player -> {
                if (gameData != null) {
                    ConfigServerToClientNetworking.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            ConfigServerToClientNetworking.createPack(gameData)
                    );
                }
                if (gameProperty != null) {
                    ConfigServerToClientNetworking.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            ConfigServerToClientNetworking.createPack(gameProperty)
                    );
                }
                if (gameRule != null) {
                    ConfigServerToClientNetworking.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            ConfigServerToClientNetworking.createPack(gameRule)
                    );
                }
            }));
        }
    }

    public static void reloadGlobalServer(MinecraftServer server) {
        if (LogicalUtils.isServer()) {
            JsonObject common = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.COMMON);
            if (common != null) {
                UninfectedZoneConfig.loadAndFixJson(common, ConfigType.COMMON);
                ConfigFileUtils.saveConfig(ConfigType.COMMON, common);
            }
        }
    }

    public static void loadAndFixJson(JsonObject jsonObject, ConfigType configType) {
        if (jsonObject.get("type").isJsonNull()) return;
        if (!jsonObject.get("type").getAsString().equals(configType.getName())) return;
        JsonManager jsonManager = new JsonManager(jsonObject);
        HashMap<String, ConfigHolder<?>> config = CONFIGS_HOLDERS.get(configType);
        config.forEach(((s, configHolder) -> {
            configHolder.loadFromJson(jsonManager);
        }));
    }

    public static void reloadGlobal() {
        if (LogicalUtils.isServer()) {
            JsonObject common = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.COMMON);
            if (common != null) {
                UninfectedZoneConfig.loadAndFixJson(common, ConfigType.COMMON);
                ConfigFileUtils.saveConfig(ConfigType.COMMON, common);
            }
        }
        if (LogicalUtils.isClient()) {
            JsonObject client = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.CLIENT);
            if (client != null) {
                UninfectedZoneConfig.loadAndFixJson(client, ConfigType.CLIENT);
                ConfigFileUtils.saveConfig(ConfigType.CLIENT, client);
            }
        }
    }

}
