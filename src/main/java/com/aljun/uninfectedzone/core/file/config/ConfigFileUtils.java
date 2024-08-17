package com.aljun.uninfectedzone.core.file.config;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.config.ConfigType;
import com.aljun.uninfectedzone.core.file.FileUtils;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;

import java.io.IOException;

public class ConfigFileUtils {
    public static final LevelResource UNINFECTED_ZONE = new LevelResource(UninfectedZone.MOD_ID);
    public static final Logger LOGGER = LogUtils.getLogger();

    public static JsonObject readConfigOrCreateBlank(ConfigType configType) {
        try {
            JsonObject jsonObject = null;
            String path = null;
            if (configType.is(ConfigType.COMMON)) {
                path = getCommonPath();
            } else if (configType.is(ConfigType.CLIENT)) {
                path = getClientPath();
            }
            if (path != null) {
                jsonObject = FileUtils.loadJsonFileOrCreate(path, () -> attachInfo(new JsonObject(), configType));
            }
            if (jsonObject == null) {
                return null;
            } else {
                if (jsonObject.get("type").isJsonNull()) {
                    LOGGER.error("Read Failed - illegal json: NO TYPE,\n creating and replacing new one...");
                    jsonObject = attachInfo(new JsonObject(), configType);
                    FileUtils.saveJsonFile(path, jsonObject);
                }
                if (!jsonObject.get("type").getAsString().equals(configType.getName())) {
                    LOGGER.error("Read Failed - illegal json: WRONG TYPE,\n creating and replacing new one...");
                    jsonObject = attachInfo(new JsonObject(), configType);
                    FileUtils.saveJsonFile(path, jsonObject);
                }
                return jsonObject;
            }
        } catch (IOException e) {
            LOGGER.error("Read Failed :{}", e.toString());
        }
        return null;
    }

    public static String getCommonPath() {
        return gamePath() + "\\global\\server\\" + ConfigType.COMMON.getName() + ".json";
    }

    public static String getClientPath() {
        return gamePath() + "\\global\\client\\" + ConfigType.CLIENT.getName() + ".json";
    }

    private static JsonObject attachInfo(JsonObject jsonObject, ConfigType configType) {
        jsonObject.addProperty("type", configType.getName());
        return jsonObject;
    }

    public static String gamePath() {
        return UninfectedZone.getAbsPath();
    }

    public static JsonObject readConfigOrCreateBlank(ConfigType configType, MinecraftServer server) {
        try {
            JsonObject jsonObject = null;
            String path = null;
            if (configType.is(ConfigType.GAME_RULE)) {
                path = getGameRulePath(server);
            } else if (configType.is(ConfigType.GAME_DATA)) {
                path = getGameDataPath(server);
            } else if (configType.is(ConfigType.GAME_PROPERTY)) {
                path = getGamePropertyPath(server);
            }
            if (path != null) {
                jsonObject = FileUtils.loadJsonFileOrCreate(path, () -> attachInfo(new JsonObject(), configType));
            }
            if (jsonObject == null) {
                return null;
            } else {
                if (jsonObject.get("type").isJsonNull()) {
                    LOGGER.error("Read From Saves Failed - illegal json: NO TYPE,\n creating and replacing new one...");
                    jsonObject = attachInfo(new JsonObject(), configType);
                    FileUtils.saveJsonFile(path, jsonObject);
                    return jsonObject;
                }
                if (!jsonObject.get("type").getAsString().equals(configType.getName())) {
                    LOGGER.error("Read From Saves Failed - illegal json: WRONG TYPE,\n creating and replacing new one...");
                    jsonObject = attachInfo(new JsonObject(), configType);
                    FileUtils.saveJsonFile(path, jsonObject);
                    return jsonObject;
                }
                return jsonObject;
            }
        } catch (IOException e) {
            LOGGER.info("Read From Saves Failed :{}", e.toString());
        }
        return null;
    }

    public static String getGameRulePath(MinecraftServer server) {
        return worldPath(server) + "\\config\\server\\" + ConfigType.GAME_RULE.getName() + ".json";
    }

    public static String getGameDataPath(MinecraftServer server) {
        return worldPath(server) + "\\config\\server\\" + ConfigType.GAME_DATA.getName() + ".json";

    }

    public static String getGamePropertyPath(MinecraftServer server) {
        return worldPath(server) + "\\config\\server\\" + ConfigType.GAME_PROPERTY.getName() + ".json";
    }

    public static String worldPath(MinecraftServer server) {
        return server.getWorldPath(UNINFECTED_ZONE).toFile().getAbsolutePath().replace("\\.", "");
    }

    public static void saveConfig(ConfigType configType, JsonObject jsonObject) {
        try {
            if (configType.is(ConfigType.COMMON)) {
                FileUtils.saveJsonFile(getCommonPath(), jsonObject);
            } else if (configType.is(ConfigType.CLIENT)) {
                FileUtils.saveJsonFile(getClientPath(), jsonObject);
            }
        } catch (IOException e) {
            LOGGER.info("Save Failed :{}", e.toString());
        }
    }

    public static void saveConfig(ConfigType configType, JsonObject jsonObject, MinecraftServer server) {
        try {
            if (configType.is(ConfigType.GAME_RULE)) {
                FileUtils.saveJsonFile(getGameRulePath(server), jsonObject);
            } else if (configType.is(ConfigType.GAME_PROPERTY)) {
                FileUtils.saveJsonFile(getGamePropertyPath(server), jsonObject);
            } else if (configType.is(ConfigType.GAME_DATA)) {
                FileUtils.saveJsonFile(getGameDataPath(server), jsonObject);
            }
        } catch (IOException e) {
            LOGGER.info("Save World Failed :{}", e.toString());
        }
    }

    public static void saveConfig(ConfigType configType, MinecraftServer server, JsonObject jsonObject) {
        try {
            if (configType.is(ConfigType.GAME_RULE)) {
                FileUtils.saveJsonFile(getGameRulePath(server), jsonObject);
            } else if (configType.is(ConfigType.GAME_DATA)) {
                FileUtils.saveJsonFile(getGamePropertyPath(server), jsonObject);
            } else if (configType.is(ConfigType.GAME_PROPERTY)) {
                FileUtils.saveJsonFile(getGamePropertyPath(server), jsonObject);
            }
        } catch (IOException e) {
            LOGGER.info("Save From ServerLevel Failed :{}", e.toString());
        }
    }

}
