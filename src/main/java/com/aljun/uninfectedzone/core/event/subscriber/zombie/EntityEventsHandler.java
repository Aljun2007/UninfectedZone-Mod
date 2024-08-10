package com.aljun.uninfectedzone.core.event.subscriber.zombie;

import com.aljun.uninfectedzone.core.game.GameUtils;
import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityEventsHandler {
    @SubscribeEvent
    public static void onVillagerInfectedVanilla(LivingConversionEvent.Pre event) {
        if (GameUtils.isDisabled()) {
            if (event.getOutcome().equals(EntityType.ZOMBIE_VILLAGER) && event.getEntityLiving() instanceof Villager) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onZombieHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().getLevel().isClientSide()) return;
        if (event.getEntityLiving() instanceof Mob mob) {
            if (ZombieUtils.isLoaded(mob)) {
                ZombieMainGoal mainGoal = ZombieUtils.getMainGoalOrAbsent(mob);
                if (mainGoal != null) {
                    mainGoal.broadcast(ZombieMainGoal.BroadcastType.HURT);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onZombieDeath(LivingDeathEvent event) {
        if (event.getEntityLiving().getLevel().isClientSide()) return;
        if (event.getEntityLiving() instanceof Mob mob) {
            if (ZombieUtils.isLoaded(mob)) {
                ZombieMainGoal mainGoal = ZombieUtils.getMainGoalOrAbsent(mob);
                if (mainGoal != null) {
                    mainGoal.broadcast(ZombieMainGoal.BroadcastType.DEATH);
                }
            }
        }
    }
}
