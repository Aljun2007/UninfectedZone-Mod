package com.aljun.uninfectedzone.core.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();

    @Nullable
    public static JsonObject loadJsonFile(String path) {
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            return GSON.fromJson(reader, JsonObject.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static void saveJsonFile(String path, JsonObject jsonObject) {
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(path, false));
            GSON.toJson(jsonObject, writer);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }


}
