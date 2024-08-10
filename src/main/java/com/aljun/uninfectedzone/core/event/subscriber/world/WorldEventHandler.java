package com.aljun.uninfectedzone.core.event.subscriber.world;

import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.game.GameUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WorldEventHandler {
    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save event) {
        if (event.getWorld() instanceof ServerLevel) {
            UninfectedZoneConfig.saveWorld(event.getWorld().getServer());
            GameUtils.save(event.getWorld().getServer());
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel) {
            UninfectedZoneConfig.loadWorld(event.getWorld().getServer());
            GameUtils.load(event.getWorld().getServer());
        }
    }
}
