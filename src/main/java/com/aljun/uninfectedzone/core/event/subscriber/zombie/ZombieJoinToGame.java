package com.aljun.uninfectedzone.core.event.subscriber.zombie;

import com.aljun.uninfectedzone.core.event.provider.zombie.ZombieSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ZombieJoinToGame {

    @SubscribeEvent
    public static void onZombieSpawn(ZombieSpawnEvent event) {

    }
}
