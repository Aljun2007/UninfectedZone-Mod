package com.aljun.uninfectedzone.core.config;

import com.aljun.uninfectedzone.core.file.config.ConfigFileUtils;
import com.aljun.uninfectedzone.core.network.ConfigJsonNetworking;
import com.aljun.uninfectedzone.core.utils.JsonManager;
import com.aljun.uninfectedzone.core.utils.VarSet;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Supplier;


public class UninfectedZoneConfig {
    private static final HashMap<ConfigType, HashMap<String, ConfigHolder<?>>> CONFIGS_HOLDERS = new HashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<ConfigSet<?>> CONFIG_SETS = new ArrayList<>();
    private static boolean build = false;

    static {
        for (ConfigType type : ConfigType.values()) {
            CONFIGS_HOLDERS.put(type, new HashMap<>());
        }
    }

    public static List<ConfigSet<?>> createAllSetsList() {
        return new ArrayList<>(CONFIG_SETS);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(@NotNull ConfigSet<T> configSet) {
        return (T) CONFIGS_HOLDERS.get(configSet.CONFIG_TYPE).get(configSet.VAR_SET.ID).get();
    }

    @SuppressWarnings("unchecked")
    public static <T> Supplier<T> getSupplier(@NotNull ConfigSet<T> configSet) {
        ConfigHolder<T> configHolder = (ConfigHolder<T>) CONFIGS_HOLDERS.get(configSet.CONFIG_TYPE).get(configSet.VAR_SET.ID);
        return configHolder::get;
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean setOrDefault(@NotNull ConfigSet<T> configSet, T t) {
        return ((ConfigHolder<T>) CONFIGS_HOLDERS.get(configSet.CONFIG_TYPE).get(configSet.VAR_SET.ID)).setOrDefault(t);
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean setOrOrigin(@NotNull ConfigSet<T> configSet, T t) {
        return ((ConfigHolder<T>) CONFIGS_HOLDERS.get(configSet.CONFIG_TYPE).get(configSet.VAR_SET.ID)).setOrOrigin(t);
    }

    public static <T> Builder<T> builder(VarSet<T> varSet, ConfigType configType) {
        return !build ? new Builder<>(varSet, configType) : null;
    }

    public static void saveWorld(MinecraftServer server) {
        JsonObject gameRule = UninfectedZoneConfig.toJsonObject(ConfigType.GAME_RULE);
        UninfectedZoneConfig.writeToJson(gameRule, ConfigType.GAME_RULE);
        ConfigFileUtils.saveConfig(ConfigType.GAME_RULE, gameRule, server);

        JsonObject gameProperty = UninfectedZoneConfig.toJsonObject(ConfigType.GAME_PROPERTY);
        UninfectedZoneConfig.writeToJson(gameProperty, ConfigType.GAME_PROPERTY);
        ConfigFileUtils.saveConfig(ConfigType.GAME_PROPERTY, gameProperty, server);

        JsonObject gameData = UninfectedZoneConfig.toJsonObject(ConfigType.GAME_DATA);
        UninfectedZoneConfig.writeToJson(gameData, ConfigType.GAME_DATA);
        ConfigFileUtils.saveConfig(ConfigType.GAME_DATA, gameData, server);
    }

    public static JsonObject toJsonObject(ConfigType configType) {
        JsonObject object = new JsonObject();
        object.addProperty("type", configType.getName());
        writeToJson(object, configType);
        return object;
    }

    public static void writeToJson(JsonObject jsonObject, ConfigType configType) {
        if (jsonObject.get("type").isJsonNull()) return;
        if (!jsonObject.get("type").getAsString().equals(configType.getName())) return;
        JsonManager jsonManager = new JsonManager(jsonObject);
        HashMap<String, ConfigHolder<?>> config = CONFIGS_HOLDERS.get(configType);
        config.forEach((s, configHolder) -> {
            configHolder.saveToJson(jsonManager);
        });
    }

    public static void saveGlobal() {
        JsonObject common = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.COMMON);
        if (common != null) {
            UninfectedZoneConfig.writeToJson(common, ConfigType.COMMON);
            ConfigFileUtils.saveConfig(ConfigType.COMMON, common);
        }
    }

    public static <T> void setDefault(ConfigSet<T> configSet) {
        CONFIGS_HOLDERS.get(configSet.CONFIG_TYPE).get(configSet.VAR_SET.ID).setDefault();
    }

    public static void sendToClient(ServerPlayer player) {
        ConfigJsonNetworking.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                ConfigJsonNetworking.createPack(toJsonObject(ConfigType.COMMON))
        );
        ConfigJsonNetworking.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                ConfigJsonNetworking.createPack(toJsonObject(ConfigType.GAME_RULE))
        );
        ConfigJsonNetworking.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                ConfigJsonNetworking.createPack(toJsonObject(ConfigType.GAME_PROPERTY))
        );
        ConfigJsonNetworking.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                ConfigJsonNetworking.createPack(toJsonObject(ConfigType.GAME_DATA))
        );
    }

    public static <T> @Nullable ConfigSet<T> register(Builder<T> builder) {
        if (!build) {
            ConfigSet<T> configSet = new ConfigSet<>(builder.varSet, builder.configType);
            configSet.setDescription(builder.description);
            configSet.setActive(builder.active);
            CONFIGS_HOLDERS.get(builder.configType).put(builder.varSet.ID, new ConfigHolder<>(configSet));
            CONFIG_SETS.add(configSet);
            return configSet;
        } else {
            LOGGER.error("Has already stopped registering");
            return null;
        }
    }

    public static void stopRegister() {
        LOGGER.warn("Stopping registering");
        build = true;
    }

    public static void reloadAll(MinecraftServer server) {
        loadWorld(server);
        loadGlobal();
    }

    public static void loadWorld(MinecraftServer server) {
        JsonObject gameRule = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.GAME_RULE, server);
        if (gameRule != null) {
            UninfectedZoneConfig.loadAndFixJson(gameRule, ConfigType.GAME_RULE);
            ConfigFileUtils.saveConfig(ConfigType.GAME_RULE, gameRule, server);
        }

        JsonObject gameProperty = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.GAME_PROPERTY, server);
        if (gameProperty != null) {
            UninfectedZoneConfig.loadAndFixJson(gameProperty, ConfigType.GAME_PROPERTY);
            ConfigFileUtils.saveConfig(ConfigType.GAME_PROPERTY, gameProperty, server);
        }

        JsonObject gameData = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.GAME_DATA, server);
        if (gameData != null) {
            UninfectedZoneConfig.loadAndFixJson(gameData, ConfigType.GAME_DATA);
            ConfigFileUtils.saveConfig(ConfigType.GAME_DATA, gameData, server);
        }

        server.getPlayerList().getPlayers().forEach(player -> {
            if (gameData != null) {
                ConfigJsonNetworking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        ConfigJsonNetworking.createPack(gameData)
                );
            }
            if (gameProperty != null) {
                ConfigJsonNetworking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        ConfigJsonNetworking.createPack(gameProperty)
                );
            }
            if (gameRule != null) {
                ConfigJsonNetworking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        ConfigJsonNetworking.createPack(gameRule)
                );
            }
        });
    }

    public static void loadGlobal() {
        JsonObject common = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.COMMON);
        if (common != null) {
            UninfectedZoneConfig.loadAndFixJson(common, ConfigType.COMMON);
            ConfigFileUtils.saveConfig(ConfigType.COMMON, common);
        }
    }

    public static void loadAndFixJson(JsonObject jsonObject, ConfigType configType) {
        if (jsonObject.get("type").isJsonNull()) return;
        if (!jsonObject.get("type").getAsString().equals(configType.getName())) return;
        JsonManager jsonManager = new JsonManager(jsonObject);
        HashMap<String, ConfigHolder<?>> config = CONFIGS_HOLDERS.get(configType);
        config.forEach((s, configHolder) -> configHolder.loadFromJsonOrDefault(jsonManager));
    }

    public static void receive(JsonObject jsonObject) {
        if (jsonObject.get("type").isJsonNull()) return;
        String name = jsonObject.get("type").getAsString();
        Optional<ConfigType> type = Arrays.stream(ConfigType.values()).filter(type1 -> type1.getName().equals(name)).findFirst();
        type.ifPresent(configType -> {
            if (!configType.is(ConfigType.CLIENT)) {
                UninfectedZoneConfig.loadOrAbsent(jsonObject, configType);
            }
        });
    }

    public static void loadOrAbsent(JsonObject jsonObject, ConfigType configType) {
        if (jsonObject.get("type").isJsonNull()) return;
        if (!jsonObject.get("type").getAsString().equals(configType.getName())) return;
        JsonManager jsonManager = new JsonManager(jsonObject);
        HashMap<String, ConfigHolder<?>> config = CONFIGS_HOLDERS.get(configType);
        config.forEach((s, configHolder) -> configHolder.loadFromJsonOrAbsent(jsonManager));
    }

    public static void init() {

        JsonObject common = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.COMMON);
        if (common != null) {
            UninfectedZoneConfig.loadAndFixJson(common, ConfigType.COMMON);
            ConfigFileUtils.saveConfig(ConfigType.COMMON, common);
        }

        JsonObject client = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.CLIENT);
        if (client != null) {
            UninfectedZoneConfig.loadAndFixJson(client, ConfigType.CLIENT);
            ConfigFileUtils.saveConfig(ConfigType.CLIENT, client);
        }

    }

    public static class Builder<T> {
        public final ConfigType configType;
        public final VarSet<T> varSet;
        public String description = null;
        public Supplier<Boolean> active = () -> true;

        Builder(VarSet<T> varSet, ConfigType configType) {
            this.varSet = varSet;
            this.configType = configType;
        }

        public Builder<T> setDescription() {
            this.description = "screen.config." + varSet.getNameSpace() + ".description";
            return this;
        }

        public ConfigSet<T> build() {
            return register(this);
        }

        public Builder<T> setActive(Supplier<Boolean> booleanSupplier) {
            this.active = booleanSupplier;
            return this;
        }
    }

}
