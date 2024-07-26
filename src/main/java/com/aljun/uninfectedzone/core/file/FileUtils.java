package com.aljun.uninfectedzone.core.file;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.*;

public class FileUtils {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();

    public static JsonObject loadJsonFileOrCreate(String path) {
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            JsonObject object = GSON.fromJson(reader, JsonObject.class);
            return object == null ? new JsonObject() : object;
        } catch (FileNotFoundException e) {
            LOGGER.error(e.toString());
            return new JsonObject();
        }
    }

    public static void saveJsonFile(String path, JsonObject jsonObject) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.error(e.toString());
            }
        }
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(path, false));
            GSON.toJson(jsonObject, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    @Nullable
    public static JsonObject loadJsonFile(String path) {
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            return GSON.fromJson(reader, JsonObject.class);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.toString());
            return null;
        }
    }


}
