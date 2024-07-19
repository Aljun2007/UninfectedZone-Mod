package com.aljun.uninfectedzone.core.event.subscriber;

import com.aljun.uninfectedzone.core.threads.ZombiePathCreating;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SeverTick {
    @SubscribeEvent
    public static void severTick(TickEvent.ServerTickEvent event) {
        ZombiePathCreating.tick();
    }
}
