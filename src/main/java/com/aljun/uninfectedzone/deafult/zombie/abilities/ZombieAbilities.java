package com.aljun.uninfectedzone.deafult.zombie.abilities;

import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ZombieAbilities {
    public static Supplier<ZombieAbility> BREAKING;
    public static Supplier<ZombieAbility> PATH_CONSTRUCTING;


    public static void register(IForgeRegistry<ZombieAbility> registry) {
        BREAKING = register(registry, Breaking::new, Breaking.KEY);
        PATH_CONSTRUCTING = register(registry, PathConstructing::new, PathConstructing.KEY);
    }

    private static Supplier<ZombieAbility> register(IForgeRegistry<ZombieAbility> registry, Supplier<ZombieAbility> instance, String key) {
        ZombieAbility zombieAbility = instance.get().setRegistryName(key);
        registry.register(zombieAbility);
        return () -> zombieAbility;
    }

}
