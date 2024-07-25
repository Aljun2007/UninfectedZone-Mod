package com.aljun.uninfectedzone.core.utils;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class JsonManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    public final JsonObject jsonObject;

    public JsonManager(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public <T> void refresh(VarSet<T> varSet) {
        final JsonObject[] jsonObjects = {jsonObject};
        varSet.NAMESPACE.forEach((var1) -> {
            if (!jsonObjects[0].has(var1))
                jsonObjects[0].add(var1, new JsonObject());
            jsonObjects[0] = (JsonObject) jsonObjects[0].get(var1);
        });

    }

    public <T> void write(VarSet<T> varSet, T var) {
        if (!varSet.verify(var)) {
            LOGGER.error("", new IllegalArgumentException("Value : " + var.toString() + " is illegal, replaced : " + varSet.defaultVar()));
            var = varSet.defaultVar();
        }
        final JsonObject[] jsonObjects = {jsonObject};
        varSet.NAMESPACE.forEach((var1) -> {
            if (!jsonObjects[0].has(var1))
                jsonObjects[0].add(var1, new JsonObject());
            jsonObjects[0] = (JsonObject) jsonObjects[0].get(var1);
        });
        varSet.varType.addToJsonObject(jsonObjects[0], varSet.KEY, varSet.defaultVar());
    }

    public <T> T read(VarSet<T> varSet) {
        final JsonObject[] jsonObjects = {jsonObject};
        varSet.NAMESPACE.forEach((var1) -> {
            if (!jsonObjects[0].has(var1))
                jsonObjects[0].add(var1, new JsonObject());
            jsonObjects[0] = (JsonObject) jsonObjects[0].get(var1);
        });
        if (!jsonObjects[0].has(varSet.KEY)) {
            varSet.varType.addToJsonObject(jsonObjects[0], varSet.KEY, varSet.defaultVar());
        }
        T t = varSet.varType.getFromJsonObject(jsonObjects[0], varSet.KEY);
        return t == null ? varSet.defaultVar() : t;
    }


}
