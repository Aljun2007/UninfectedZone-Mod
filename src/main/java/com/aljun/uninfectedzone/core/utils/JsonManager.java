package com.aljun.uninfectedzone.core.utils;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public record JsonManager(JsonObject jsonObject) {
    private static final Logger LOGGER = LogUtils.getLogger();

    public <T> void refresh(VarSet<T> varSet) {
        final JsonObject[] jsonObjects = {jsonObject};
        varSet.NAMESPACE.forEach(var1 -> {
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
        varSet.varType.serialize(jsonObjects[0], varSet.KEY, varSet.defaultVar());
    }

    public <T> T readOrAbsent(VarSet<T> varSet, T replace) {
        final JsonObject[] jsonObjects = {jsonObject};
        varSet.NAMESPACE.forEach(var1 -> {
            if (!jsonObjects[0].has(var1))
                return;
            jsonObjects[0] = (JsonObject) jsonObjects[0].get(var1);
        });
        if (!jsonObjects[0].has(varSet.KEY)) {
            return replace;
        }
        T t = varSet.varType.deserialize(jsonObjects[0], varSet.KEY);
        return t == null ? replace : t;
    }

    public <T> T readOrDefalut(VarSet<T> varSet) {
        return this.readOrReplace(varSet, varSet.defaultVar());
    }

    public <T> T readOrReplace(VarSet<T> varSet, T replace) {
        final JsonObject[] jsonObjects = {jsonObject};
        varSet.NAMESPACE.forEach(var1 -> {
            if (!jsonObjects[0].has(var1))
                jsonObjects[0].add(var1, new JsonObject());
            jsonObjects[0] = (JsonObject) jsonObjects[0].get(var1);
        });
        if (!jsonObjects[0].has(varSet.KEY)) {
            varSet.varType.serialize(jsonObjects[0], varSet.KEY, varSet.defaultVar());
        }
        T t = varSet.varType.deserialize(jsonObjects[0], varSet.KEY);
        return t == null ? replace : t;
    }

    public <T> T readOrCreate(VarSet<T> varSet) {
        final JsonObject[] jsonObjects = {jsonObject};
        varSet.NAMESPACE.forEach(var1 -> {
            if (!jsonObjects[0].has(var1))
                jsonObjects[0].add(var1, new JsonObject());
            jsonObjects[0] = (JsonObject) jsonObjects[0].get(var1);
        });
        if (!jsonObjects[0].has(varSet.KEY)) {
            varSet.varType.serialize(jsonObjects[0], varSet.KEY, varSet.defaultVar());
        }
        T t = varSet.varType.deserialize(jsonObjects[0], varSet.KEY);
        if (t == null) {
            t = varSet.defaultVar();
            varSet.varType.serialize(jsonObjects[0], varSet.KEY, varSet.defaultVar());
        }
        return t;
    }


}
