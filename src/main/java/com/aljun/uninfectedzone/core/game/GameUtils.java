package com.aljun.uninfectedzone.core.game;

import com.aljun.uninfectedzone.api.registry.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.core.file.GameFileUtils;
import com.aljun.uninfectedzone.core.game.mode.GameMode;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import java.util.Objects;

public class GameUtils {

    private static final Logger LOGGER = LogUtils.getLogger();


    private static GameMode gameMode = GameMode.DISABLED.get().get();

    public static void load(MinecraftServer server) {
        JsonObject object = GameFileUtils.readOrCreateBlank(server);
        loadFromJson(object);
    }

    private static void loadFromJson(JsonObject jsonObject) {

        try {
            if (!jsonObject.has("mode")) {
                jsonObject.addProperty("mode", Objects.requireNonNull(gameMode == null ? GameMode.DISABLED.get().getRegistryName() : gameMode.name).toString());
            }
            if (!jsonObject.get("mode").isJsonPrimitive()) {
                jsonObject.addProperty("mode", Objects.requireNonNull(gameMode == null ? GameMode.DISABLED.get().getRegistryName() : gameMode.name).toString());
            }
            ResourceLocation location = new ResourceLocation(jsonObject.get("mode").getAsString());
            if (gameMode.name.equals(location)) {
                GameMode.Builder builder = UninfectedZoneRegistry.GAME_MODE.get().getValue(location);
                if (builder != null) {
                    gameMode = builder.get();
                }
            }

            if (!jsonObject.has("mode_data")) {
                jsonObject.add("mode_data", new JsonObject());
            }
            if (!jsonObject.get("mode_data").isJsonObject()) {
                jsonObject.add("mode_data", new JsonObject());
            }

            JsonObject data = jsonObject.getAsJsonObject("mode_data");
            gameMode.load(data);

        } catch (Throwable throwable) {
            LOGGER.error("Load Game Failed : {}", throwable.toString());
        }


    }

    public static void save(MinecraftServer server) {
        JsonObject object = new JsonObject();
        saveToJson(object);
        GameFileUtils.save(object, server);

    }

    private static void saveToJson(JsonObject jsonObject) {
        try {
            jsonObject.addProperty("mode", Objects.requireNonNull(gameMode == null ? GameMode.DISABLED.get().getRegistryName() : gameMode.name).toString());
            if (!jsonObject.has("mode_data")) {
                jsonObject.add("mode_data", new JsonObject());
            }
            if (!jsonObject.get("mode_data").isJsonObject()) {
                jsonObject.add("mode_data", new JsonObject());
            }
            JsonObject data = jsonObject.getAsJsonObject("mode_data");
            gameMode.save(data);
        } catch (Throwable throwable) {
            LOGGER.error("Save Game Failed : {}", throwable.toString());
        }
    }

    public static GameMode getGameMode() {
        return gameMode;
    }

    public static boolean isDisabled() {
        return gameMode.name.equals(GameMode.DISABLED.get().getRegistryName());
    }
}
