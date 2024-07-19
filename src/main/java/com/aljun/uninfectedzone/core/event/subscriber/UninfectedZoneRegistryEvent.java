package com.aljun.uninfectedzone.core.event.subscriber;

import com.aljun.uninfectedzone.core.forgeRegister.UninfectedZoneRegistry;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UninfectedZoneRegistryEvent {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        UninfectedZoneRegistry.register(event);
    }


}
