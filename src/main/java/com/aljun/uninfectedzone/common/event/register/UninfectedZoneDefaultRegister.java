package com.aljun.uninfectedzone.common.event.register;

import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import com.aljun.uninfectedzone.common.zombie.abilities.ZombieAbilities;
import com.aljun.uninfectedzone.common.zombie.attribute.ZombieAttributes;
import com.aljun.uninfectedzone.common.zombie.zombieLikes.ZombieLikes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UninfectedZoneDefaultRegister {
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
    public static void registerStructureFeatures(RegistryEvent.Register<StructureFeature<?>> event) {

    }

}
