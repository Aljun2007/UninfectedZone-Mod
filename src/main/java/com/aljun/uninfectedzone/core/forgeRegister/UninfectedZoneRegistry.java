package com.aljun.uninfectedzone.core.forgeRegister;

import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import com.aljun.uninfectedzone.core.zombie.speedtypes.ZombieSpeedType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class UninfectedZoneRegistry {
    private static final int MAX_VARINT = Integer.MAX_VALUE - 1;
    public static Supplier<IForgeRegistry<ZombieLike>> ZOMBIE_LIKES;
    public static Supplier<IForgeRegistry<ZombieSpeedType>> ZOMBIE_SPEED_TYPES;
    public static Supplier<IForgeRegistry<ZombieAbility>> ZOMBIE_ABILITIES;
//    public static Supplier<IForgeRegistry<InfectionConvertType>> INFECTION_CONVERT_TYPES;

    public static void register(NewRegistryEvent event) {

        ZOMBIE_LIKES = event.create(
                new RegistryBuilder<ZombieLike>()
                        .setName(UninfectedZoneRegistry.Keys.ZOMBIE_LIKES.location())
                        .setType(ZombieLike.class)
                        .setMaxID(Integer.MAX_VALUE - 1)
                        .setDefaultKey(new ResourceLocation("dummy"))
        );
        ZOMBIE_SPEED_TYPES = event.create(new RegistryBuilder<ZombieSpeedType>().setName(
                        Keys.ZOMBIE_SPEED_TYPES.location())
                .setType(ZombieSpeedType.class)
                .setMaxID(MAX_VARINT)
                .setDefaultKey(new ResourceLocation("dummy"))
        );
        ZOMBIE_ABILITIES = event.create(new RegistryBuilder<ZombieAbility>().setName(
                        Keys.ZOMBIE_ABILITIES.location())
                .setType(ZombieAbility.class)
                .setMaxID(MAX_VARINT)
                .setDefaultKey(new ResourceLocation("dummy"))
        );
//        INFECTION_CONVERT_TYPES = event.create(new RegistryBuilder<InfectionConvertType>().setName(
//                        Keys.INFECTION_CONVERT_TYPES.location())
//                .setType(InfectionConvertType.class)
//                .setMaxID(MAX_VARINT)
//                .setDefaultKey(new ResourceLocation("dummy"))
//        );

    }

    public static class Keys {
        public static final ResourceKey<Registry<ZombieLike>> ZOMBIE_LIKES = key("zombie_like");
        public static final ResourceKey<Registry<ZombieSpeedType>> ZOMBIE_SPEED_TYPES = key("zombie_speed_type");
        public static final ResourceKey<Registry<ZombieAbility>> ZOMBIE_ABILITIES = key("zombie_ability");
//        public static final ResourceKey<Registry<InfectionConvertType>> INFECTION_CONVERT_TYPES = key("infection_convert_type");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(new ResourceLocation(name));
        }
    }
}
