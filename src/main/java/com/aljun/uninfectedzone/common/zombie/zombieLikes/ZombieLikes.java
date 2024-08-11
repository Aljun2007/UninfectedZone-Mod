package com.aljun.uninfectedzone.common.zombie.zombieLikes;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ZombieLikes {

    public static Supplier<ZombieLike> VANILLA_ZOMBIE;
    public static Supplier<ZombieLike> BREAK_AND_BUILD_ZOMBIE;
    public static Supplier<ZombieLike> NORMAL_ZOMBIE;

    public static void register(IForgeRegistry<ZombieLike> registry) {
        VANILLA_ZOMBIE = register(registry, VanillaZombie::new, "vanilla_zombie");
        BREAK_AND_BUILD_ZOMBIE = register(registry, BuildAndBreakZombie::new, "break_and_build_zombie");
        NORMAL_ZOMBIE = register(registry, BuildAndBreakZombie::new, "normal_zombie");
    }

    private static Supplier<ZombieLike> register(IForgeRegistry<ZombieLike> registry, Supplier<ZombieLike> instance, String key) {
        ZombieLike zombieLike = instance.get().setRegistryName(UninfectedZone.MOD_ID, key);
        registry.register(zombieLike);
        return () -> zombieLike;
    }

}

