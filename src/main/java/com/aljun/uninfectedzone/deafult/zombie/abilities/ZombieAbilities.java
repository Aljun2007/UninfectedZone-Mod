package com.aljun.uninfectedzone.deafult.zombie.abilities;

import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ZombieAbilities {
    public static Supplier<Breaking> BREAKING;
    public static Supplier<Placing> PLACING;
    public static Supplier<PathConstructing> PATH_CONSTRUCTING;


    public static void register(IForgeRegistry<ZombieAbility> registry) {
        BREAKING = register(registry, Breaking::new, Breaking.KEY);
        PLACING = register(registry, Placing::new, Placing.KEY);
        PATH_CONSTRUCTING = register(registry, PathConstructing::new, PathConstructing.KEY);
    }

    private static <T extends ZombieAbility> Supplier<T> register(IForgeRegistry<ZombieAbility> registry, Supplier<T> instance, String key) {
        T zombieAbility = instance.get();
        zombieAbility.setRegistryName(key);
        registry.register(zombieAbility);
        return () -> zombieAbility;
    }

}
