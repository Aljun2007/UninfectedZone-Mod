package com.aljun.uninfectedzone.common.event.register;

import com.aljun.uninfectedzone.api.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.common.minecraft.entity.UninfectedZoneEntityTypes;
import com.aljun.uninfectedzone.common.sound.UninfectedZoneSounds;
import com.aljun.uninfectedzone.common.zombie.abilities.ZombieAbilities;
import com.aljun.uninfectedzone.common.zombie.attribute.ZombieAttributes;
import com.aljun.uninfectedzone.common.zombie.zombieLikes.ZombieLikes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UninfectedZoneCommonRegister {

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        UninfectedZoneEntityTypes.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerZombieLike(RegistryEvent.Register<ZombieLike> event) {
        ZombieLikes.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerZombieAbilities(RegistryEvent.Register<ZombieAbility> event) {
        ZombieAbilities.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerAttributes(RegistryEvent.Register<Attribute> event) {
        ZombieAttributes.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        UninfectedZoneSounds.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerStructureFeatures(RegistryEvent.Register<StructureFeature<?>> event) {

    }

}
