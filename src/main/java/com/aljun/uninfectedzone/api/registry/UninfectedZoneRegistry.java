package com.aljun.uninfectedzone.api.registry;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.api.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.core.game.mode.GameMode;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * <p>
 *
 * @author Aljun2007
 * @see net.minecraftforge.event.RegistryEvent
 * If you want to register some classes.
 * @see com.aljun.uninfectedzone.common.event.register.UninfectedZoneCommonRegister
 * Like this : EXAMPLE
 */

public class UninfectedZoneRegistry {
    private static final int MAX_VARINT = Integer.MAX_VALUE - 1;

    public static Supplier<IForgeRegistry<ZombieLike>> ZOMBIE_LIKES;
    public static Supplier<IForgeRegistry<ZombieAbility>> ZOMBIE_ABILITIES;

    public static Supplier<IForgeRegistry<GameMode.Builder>> GAME_MODE;

    public static void register(NewRegistryEvent event) {
        ZOMBIE_LIKES = event.create(
                new RegistryBuilder<ZombieLike>()
                        .setName(UninfectedZoneRegistry.Keys.ZOMBIE_LIKES.location())
                        .setType(ZombieLike.class)
                        .setMaxID(MAX_VARINT)
                        .setDefaultKey(new ResourceLocation(UninfectedZone.MOD_ID, "dummy"))
        );
        ZOMBIE_ABILITIES = event.create(new RegistryBuilder<ZombieAbility>().setName(
                        Keys.ZOMBIE_ABILITIES.location())
                .setType(ZombieAbility.class)
                .setMaxID(MAX_VARINT)
                .setDefaultKey(new ResourceLocation(UninfectedZone.MOD_ID, "dummy"))
        );
        GAME_MODE = event.create(new RegistryBuilder<GameMode.Builder>().setName(
                        Keys.GAME_MODE.location())
                .setType(GameMode.Builder.class)
                .setMaxID(MAX_VARINT)
                .setDefaultKey(new ResourceLocation(UninfectedZone.MOD_ID, "disabled"))
        );

    }

    public static class Keys {

        public static final ResourceKey<Registry<ZombieLike>> ZOMBIE_LIKES = key("zombie_like");
        public static final ResourceKey<Registry<ZombieAbility>> ZOMBIE_ABILITIES = key("zombie_ability");
        public static final ResourceKey<Registry<GameMode.Builder>> GAME_MODE = key("game_mode");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(new ResourceLocation(UninfectedZone.MOD_ID, name));
        }
    }
}
