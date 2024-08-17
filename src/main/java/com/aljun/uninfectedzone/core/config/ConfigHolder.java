package com.aljun.uninfectedzone.core.config;

import com.aljun.uninfectedzone.core.utils.JsonManager;

public class ConfigHolder<T> {

    public final ConfigSet<T> CONFIG_SET;
    private T var;

    ConfigHolder(ConfigSet<T> configSet) {
        this.CONFIG_SET = configSet;
        this.var = configSet.VAR_SET.defaultVar();
    }

    public T get() {
        return var;
    }

    public void loadFromJsonOrDefault(JsonManager jsonManager) {
        this.setOrDefault(jsonManager.readOrDefalut(this.CONFIG_SET.VAR_SET));
    }

    public boolean setOrDefault(T t) {
        if (this.CONFIG_SET.VAR_SET.verify(t)) {
            this.var = t;
            return true;
        } else {
            this.var = this.CONFIG_SET.VAR_SET.defaultVar();
            return false;
        }
    }

    public void loadFromJsonOrAbsent(JsonManager jsonManager) {
        this.setOrOrigin(jsonManager.readOrAbsent(this.CONFIG_SET.VAR_SET, this.var));
    }

    public boolean setOrOrigin(T t) {
        if (this.CONFIG_SET.VAR_SET.verify(t)) {
            this.var = t;
            return true;
        } else {
            return false;
        }
    }

    public void setDefault() {
        this.var = this.CONFIG_SET.VAR_SET.defaultVar();
    }

    public void saveToJson(JsonManager jsonManager) {
        jsonManager.write(this.CONFIG_SET.VAR_SET, this.var);
    }
}
