package com.aljun.uninfectedzone.common.sound;

import com.aljun.uninfectedzone.UninfectedZone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class UninfectedZoneSounds {

    public static Supplier<SoundEvent> CUSTOM_ZOMBIE_HURT;
    public static Supplier<SoundEvent> CUSTOM_ZOMBIE_AMBIENT;
    public static Supplier<SoundEvent> CUSTOM_ZOMBIE_DEATH;
    public static Supplier<SoundEvent> CUSTOM_ZOMBIE_STEP;

    public static void register(IForgeRegistry<SoundEvent> registry) {
        CUSTOM_ZOMBIE_HURT = register(registry, () -> new SoundEvent(new ResourceLocation(UninfectedZone.MOD_ID, "entity.custom_zombie.hurt")), "custom_zombie_hurt");
        CUSTOM_ZOMBIE_AMBIENT = register(registry, () -> new SoundEvent(new ResourceLocation(UninfectedZone.MOD_ID, "entity.custom_zombie.ambient")), "custom_zombie_ambient");
        CUSTOM_ZOMBIE_DEATH = register(registry, () -> new SoundEvent(new ResourceLocation(UninfectedZone.MOD_ID, "entity.custom_zombie.death")), "custom_zombie_death");
        CUSTOM_ZOMBIE_STEP = register(registry, () -> new SoundEvent(new ResourceLocation(UninfectedZone.MOD_ID, "entity.custom_zombie.step")), "custom_zombie_step");
    }

    private static Supplier<SoundEvent> register(IForgeRegistry<SoundEvent> registry, Supplier<SoundEvent> instance, String key) {
        SoundEvent soundEvent = instance.get().setRegistryName(key);
        registry.register(soundEvent);
        return () -> soundEvent;
    }
}

