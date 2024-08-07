package com.aljun.uninfectedzone.common.config;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.common.config.data.pool.ZombieLikePools;
import com.aljun.uninfectedzone.core.config.ConfigSet;
import com.aljun.uninfectedzone.core.config.ConfigType;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.data.pool.ZombieLikePool;
import com.aljun.uninfectedzone.core.utils.VarSet;

import java.util.List;
import java.util.function.Supplier;

public class ConfigSets {

    public static void register() {
        Common.register();
        Client.register();
    }

    private static <T> Supplier<ConfigSet<T>> register(ConfigType configType, VarSet<T> varSet) {
        ConfigSet<T> configSet = UninfectedZoneConfig.register(varSet, configType);
        return () -> configSet;
    }

    public static class Common {
        public static Supplier<ConfigSet<ZombieLikePool>> DEFAULT_ZOMBIE_POOL;
        public static Supplier<ConfigSet<Double>> TEST_DOUBLE;

        private static void register() {
            DEFAULT_ZOMBIE_POOL = ConfigSets.register(ConfigType.COMMON, VarSet.builder(UninfectedZone.MOD_ID, ZombieLikePool.ZOMBIE_LIKE_POOL)
                    .defaultVar(ZombieLikePools.testPool).create("default_zombie_like_pool"));
            TEST_DOUBLE = ConfigSets.register(ConfigType.COMMON, VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.DOUBLE).defaultVar(1314d).create("test_double"));
        }
    }

    public static class Client {
        public static Supplier<ConfigSet<List<String>>> CUSTOM_ZOMBIE_TEXTURE_PATH;
        public static Supplier<ConfigSet<List<String>>> CUSTOM_ZOMBIE_SLIM_TEXTURE_PATH;

        private static void register() {
            CUSTOM_ZOMBIE_TEXTURE_PATH = ConfigSets.register(ConfigType.CLIENT, VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.STRING_LIST).defaultVar(List.of("minecraft:textures/entity/steve.png")).create("custom_zombie_texture_path"));
            CUSTOM_ZOMBIE_SLIM_TEXTURE_PATH = ConfigSets.register(ConfigType.CLIENT, VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.STRING_LIST).defaultVar(List.of("minecraft:textures/entity/alex.png")).create("custom_zombie_slim_texture_path"));
        }
    }

}
