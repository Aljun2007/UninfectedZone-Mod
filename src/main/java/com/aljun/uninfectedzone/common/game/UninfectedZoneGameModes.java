package com.aljun.uninfectedzone.common.game;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.game.mode.GameMode;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class UninfectedZoneGameModes {

    public static Supplier<GameMode> CLASSIC;

    public static void register(IForgeRegistry<GameMode.Builder> registry) {

    }

    private static <T extends GameMode.Builder> Supplier<T> register(IForgeRegistry<GameMode.Builder> registry, Supplier<T> instance, String key) {
        T gameMode = instance.get();
        gameMode.setRegistryName(UninfectedZone.MOD_ID, key);
        registry.register(gameMode);
        return () -> gameMode;
    }
}
