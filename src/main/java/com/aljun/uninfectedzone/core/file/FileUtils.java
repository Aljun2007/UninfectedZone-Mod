package com.aljun.uninfectedzone.core.file;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.*;
import java.util.function.Supplier;

public class FileUtils {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();

    public static JsonObject loadJsonFileOrCreate(String path, Supplier<JsonObject> newOne) throws IOException {

        JsonObject object = null;
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            object = GSON.fromJson(reader, JsonObject.class);
        } catch (FileNotFoundException e) {
            object = newOne.get();
            saveJsonFile(path, object);
        }
        return object;
    }

    public static void saveJsonFile(String path, JsonObject jsonObject) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        JsonWriter writer = new JsonWriter(new FileWriter(file, false));
        GSON.toJson(jsonObject, writer);
        writer.flush();
        writer.close();
    }

    public static void deleteJsonFile(String path) {
        File file = new File(path);
        file.delete();
    }


}
