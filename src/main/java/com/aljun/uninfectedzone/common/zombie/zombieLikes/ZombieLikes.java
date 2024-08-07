package com.aljun.uninfectedzone.common.zombie.zombieLikes;

import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ZombieLikes {

    public static Supplier<ZombieLike> VANILLA_ZOMBIE;
    public static Supplier<ZombieLike> TEST_ZOMBIE;

    public static void register(IForgeRegistry<ZombieLike> registry) {
        VANILLA_ZOMBIE = register(registry, VanillaZombie::new, "vanilla_zombie");
        TEST_ZOMBIE = register(registry, TestZombie::new, "test_zombie");
    }

    private static Supplier<ZombieLike> register(IForgeRegistry<ZombieLike> registry, Supplier<ZombieLike> instance, String key) {
        ZombieLike zombieLike = instance.get().setRegistryName(key);
        registry.register(zombieLike);
        return () -> zombieLike;
    }

}

