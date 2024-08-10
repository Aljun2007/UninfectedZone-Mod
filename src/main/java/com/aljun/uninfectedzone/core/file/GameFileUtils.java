package com.aljun.uninfectedzone.core.file;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import java.io.IOException;

import static com.aljun.uninfectedzone.core.file.config.ConfigFileUtils.UNINFECTED_ZONE;

public class GameFileUtils {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static JsonObject readOrCreateBlank(MinecraftServer server) {
        try {
            String path = getPath(server);
            return FileUtils.loadJsonFileOrCreate(path, () -> attachInfo(new JsonObject()));
        } catch (IOException e) {
            LOGGER.error("Read Failed :{}", e.toString());
        }
        return null;
    }

    public static String getPath(MinecraftServer server) {
        return server.getWorldPath(UNINFECTED_ZONE).toFile().getAbsolutePath().replace("\\.", "") + "\\game.json";
    }

    private static JsonObject attachInfo(JsonObject jsonObject) {
        return jsonObject;
    }

    public static void save(JsonObject jsonObject, MinecraftServer server) {
        try {
            FileUtils.saveJsonFile(getPath(server), jsonObject);
        } catch (IOException e) {
            LOGGER.info("Save Failed :{}", e.toString());
        }
    }


}
