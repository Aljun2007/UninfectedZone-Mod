package com.aljun.uninfectedzone.common.zombie.abilities;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.api.zombie.abilities.ZombieAbility;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ZombieAbilities {
    public static Supplier<Hearing> HEARING;
    public static Supplier<Breaking> BREAKING;
    public static Supplier<Placing> PLACING;
    public static Supplier<PathConstructing> PATH_CONSTRUCTING;


    public static void register(IForgeRegistry<ZombieAbility> registry) {
        BREAKING = register(registry, Breaking::new, Breaking.KEY);
        PLACING = register(registry, Placing::new, Placing.KEY);
        PATH_CONSTRUCTING = register(registry, PathConstructing::new, PathConstructing.KEY);
        HEARING = register(registry, Hearing::new, Hearing.KEY);
    }

    private static <T extends ZombieAbility> Supplier<T> register(IForgeRegistry<ZombieAbility> registry, Supplier<T> instance, String key) {
        T zombieAbility = instance.get();
        zombieAbility.setRegistryName(UninfectedZone.MOD_ID, key);
        registry.register(zombieAbility);
        return () -> zombieAbility;
    }

}
