package com.aljun.uninfectedzone.core.event.subscriber;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.config.GlobalConfigs;
import com.aljun.uninfectedzone.core.datapacks.conditions.UninfectedZoneLootItemConditions;
import com.aljun.uninfectedzone.core.forgeRegister.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.core.network.ChunkBorderCommandNetworking;
import com.aljun.uninfectedzone.core.zombie.like.DummyZombie;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UninfectedZoneRegisterEvent {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        UninfectedZoneRegistry.register(event);
    }

    @SubscribeEvent
    public static void registerZombieLike(RegistryEvent.Register<ZombieLike> event) {
        ZombieLike dummy = new DummyZombie().setRegistryName("dummy");
        event.getRegistry().register(dummy);
        ZombieLike.DUMMY = () -> dummy;
    }

    @SubscribeEvent
    public static void registerOthers(FMLCommonSetupEvent event) {
        GlobalConfigs.register();
        Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation("uninfectedzone:loaded_mod"), UninfectedZoneLootItemConditions.LOADED_MOD);
        event.enqueueWork(() -> {
            ChunkBorderCommandNetworking.registerMessage();
            UninfectedZone.afterRegister();

        });
    }

    @SubscribeEvent
    public void registerLootItemConditionType(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
    }


}
