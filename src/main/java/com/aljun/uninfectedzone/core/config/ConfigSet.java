package com.aljun.uninfectedzone.core.config;

import com.aljun.uninfectedzone.core.utils.VarSet;

public class ConfigSet<T> {

    public final VarSet<T> VAR_SET;
    public final ConfigType CONFIG_TYPE;


    ConfigSet(VarSet<T> varSet, ConfigType configType) {
        VAR_SET = varSet;
        CONFIG_TYPE = configType;
    }

}
