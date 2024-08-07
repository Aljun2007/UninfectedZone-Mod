package com.aljun.uninfectedzone.core.event.subscriber;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.api.registry.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.api.zombie.zombielike.DummyZombie;
import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.common.config.ConfigSets;
import com.aljun.uninfectedzone.core.data.loot_table.conditions.UninfectedZoneLootItemConditions;
import com.aljun.uninfectedzone.core.network.ByteNetWorking;
import com.aljun.uninfectedzone.core.network.ChunkBorderCommandNetworking;
import com.aljun.uninfectedzone.core.network.ConfigServerToClientNetworking;
import com.aljun.uninfectedzone.core.network.UninfectedChuckNetworking;
import com.mojang.logging.LogUtils;
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
        ConfigSets.register();
        UninfectedZoneLootItemConditions.register();
        event.enqueueWork(() -> {
            ChunkBorderCommandNetworking.registerMessage();
            ConfigServerToClientNetworking.registerMessage();
            UninfectedChuckNetworking.registerMessage();
            ByteNetWorking.registerMessage();
            UninfectedZone.afterRegister();
        });
    }

}
