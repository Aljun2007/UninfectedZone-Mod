package com.aljun.uninfectedzone.core.event.subscriber;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.api.registry.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.api.zombie.zombielike.DummyZombie;
import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.common.config.ConfigSets;
import com.aljun.uninfectedzone.core.data.loot_table.conditions.UninfectedZoneLootItemConditions;
import com.aljun.uninfectedzone.core.game.mode.GameMode;
import com.aljun.uninfectedzone.core.network.*;
import com.mojang.logging.LogUtils;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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
        ZombieLike dummy = new DummyZombie().setRegistryName(UninfectedZone.MOD_ID, "dummy");
        event.getRegistry().register(dummy);
        ZombieLike.DUMMY = () -> dummy;
    }

    @SubscribeEvent
    public static void registerGameMode(RegistryEvent.Register<GameMode.Builder> event) {
        event.getRegistry().register(GameMode.DISABLED.get().setRegistryName(UninfectedZone.MOD_ID, "disabled"));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerOthers(FMLCommonSetupEvent event) {
        UninfectedZoneLootItemConditions.register();
        event.enqueueWork(() -> {
            ChunkBorderCommandNetworking.registerMessage();
            ConfigJsonNetworking.registerMessage();
            ConfigEnquiringNetworking.registerMessage();
            UninfectedChuckNetworking.registerMessage();
            ByteNetWorking.registerMessage();
            UninfectedZone.afterRegister();
        });
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerOthersLater(FMLCommonSetupEvent event) {
        ConfigSets.register();
    }

}
