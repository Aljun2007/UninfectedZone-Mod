package com.aljun.uninfectedzone.core.event.provider.world;

import com.aljun.uninfectedzone.core.event.provider.UninfectedZoneEventFactory;
import com.aljun.uninfectedzone.core.utils.ZombieLoadUtils;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OnMobLoadAndSpawn {
    @SubscribeEvent
    public static void onMobSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity() instanceof Mob mob) {
            if (ZombieLoadUtils.hasLoadedIfAbsentMainGoal(mob)) {
                UninfectedZoneEventFactory.onZombieSpawn(mob);
            }
        }
    }

    @SubscribeEvent
    public static void onMobLoad(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            if (ZombieLoadUtils.hasLoadedIfAbsentMainGoal(mob)) {
                UninfectedZoneEventFactory.onZombieLoad(mob);
            }
        }
    }
}
