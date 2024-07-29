package com.aljun.uninfectedzone.core.config;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.utils.VarSet;

import java.util.function.Supplier;

public class GlobalConfigs {

    public static Supplier<ConfigSet<Boolean>> TEST_BOOLEAN;

    public static void register() {
        TEST_BOOLEAN = register(VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.BOOLEAN).defaultVar(false).create("test_boolean"));
    }

    private static <T> Supplier<ConfigSet<T>> register(VarSet<T> varSet) {
        ConfigSet<T> configSet = UninfectedZoneConfig.register(varSet, ConfigType.COMMON);
        return () -> configSet;
    }
}
