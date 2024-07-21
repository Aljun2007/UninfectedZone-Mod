package com.aljun.uninfectedzone.core.event.provider.world;

import com.aljun.uninfectedzone.core.event.provider.UninfectedZoneEventFactory;
import com.aljun.uninfectedzone.core.utils.ZombieLoadUtils;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
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
                ZombieLike zombieLike = ZombieLoadUtils.getZombieLike(mob);
                if (zombieLike != null) {
                    mob.setItemSlot(EquipmentSlot.HEAD,ItemStack.EMPTY);
                    mob.setItemSlot(EquipmentSlot.CHEST,ItemStack.EMPTY);
                    mob.setItemSlot(EquipmentSlot.LEGS,ItemStack.EMPTY);
                    mob.setItemSlot(EquipmentSlot.FEET,ItemStack.EMPTY);
                    mob.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);
                    mob.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);
                    mob.removeAllEffects();
                    zombieLike.equip(mob);
                    zombieLike.attributes(mob);
                    UninfectedZoneEventFactory.onZombieSpawn(mob);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMobLoad(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            if (ZombieLoadUtils.hasLoadedIfAbsentMainGoal(mob)) {
                ZombieLike zombieLike = ZombieLoadUtils.getZombieLike(mob);
                if (zombieLike != null) {
                    zombieLike.load(mob);
                    UninfectedZoneEventFactory.onZombieLoad(mob);
                }
            }
        }
    }
}
