package com.aljun.uninfectedzone.common.event.register;

import com.aljun.uninfectedzone.common.minecraft.entity.UninfectedZoneEntityTypes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributeRegister {

    @SubscribeEvent
    public static void registerEntityTypes(EntityAttributeCreationEvent event) {
        event.put(UninfectedZoneEntityTypes.CUSTOM_ZOMBIE.get(), Zombie.createAttributes().build());
        event.put(UninfectedZoneEntityTypes.CUSTOM_ZOMBIE_SLIM.get(), Zombie.createAttributes().build());
    }

}
