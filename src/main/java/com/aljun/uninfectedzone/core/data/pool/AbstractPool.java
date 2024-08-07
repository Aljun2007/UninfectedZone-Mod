package com.aljun.uninfectedzone.core.data.pool;

import com.google.gson.JsonObject;

public abstract class AbstractPool<T extends AbstractPool<?>> {
    public AbstractPool(JsonObject object) {
    }

    protected AbstractPool() {
    }


}
