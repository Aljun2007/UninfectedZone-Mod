package com.aljun.uninfectedzone.common.minecraft.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class UninfectedZoneEntityTypes {
    public static Supplier<EntityType<CustomZombie>> CUSTOM_ZOMBIE;
    public static Supplier<EntityType<CustomZombie>> CUSTOM_ZOMBIE_SLIM;

    public static void register(IForgeRegistry<EntityType<?>> registry) {
        CUSTOM_ZOMBIE = register(registry, () -> EntityType.Builder.of(CustomZombie::createDefault, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("custom_zombie"), "custom_zombie");
        CUSTOM_ZOMBIE_SLIM = register(registry, () -> EntityType.Builder.of(CustomZombie::createSlim, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("custom_zombie_slim"), "custom_zombie_slim");
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> Supplier<EntityType<T>> register(IForgeRegistry<EntityType<?>> registry, Supplier<EntityType<T>> instance, String key) {
        EntityType<T> entityType = (EntityType<T>) instance.get().setRegistryName(key);
        registry.register(entityType);
        return () -> entityType;
    }

}
