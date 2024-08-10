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
        GameData.register();
        GameRule.register();
        GameProperty.register();
    }

    private static <T> Supplier<ConfigSet<T>> register(ConfigType configType, VarSet<T> varSet) {
        UninfectedZoneConfig.Builder<T> builder = UninfectedZoneConfig.builder(varSet, configType);
        ConfigSet<T> configSet;
        if (builder != null) {
            configSet = builder.build();
        } else {
            configSet = null;
        }
        return () -> configSet;
    }

    public static class Common {

        public static Supplier<ConfigSet<Double>> TEST_DOUBLE;

        private static void register() {
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

    public static class GameData {
        public static Supplier<ConfigSet<ZombieLikePool>> DEFAULT_ZOMBIE_POOL;

        private static void register() {
            DEFAULT_ZOMBIE_POOL = ConfigSets.register(ConfigType.GAME_DATA, VarSet.builder(UninfectedZone.MOD_ID, ZombieLikePool.ZOMBIE_LIKE_POOL)
                    .defaultVar(ZombieLikePools.testPool).create("default_zombie_like_pool"));
        }
    }

    public static class GameProperty {
        private static void register() {
        }
    }

    public static class GameRule {
        private static void register() {
        }
    }

}
