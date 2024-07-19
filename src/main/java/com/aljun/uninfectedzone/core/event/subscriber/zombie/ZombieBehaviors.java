package com.aljun.uninfectedzone.core.event.subscriber.zombie;

import com.aljun.uninfectedzone.core.game.GameController;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ZombieBehaviors {
    @SubscribeEvent
    public static void onVillagerInfectedVanilla(LivingConversionEvent.Pre event) {
        if (GameController.isVanillaInfectionDisabled()) {
            if (event.getOutcome().equals(EntityType.ZOMBIE_VILLAGER) && event.getEntityLiving() instanceof Villager) {
                event.setCanceled(true);
            }
        }
    }
}
