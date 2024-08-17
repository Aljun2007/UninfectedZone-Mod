package com.aljun.uninfectedzone.common.event.register;

import com.aljun.uninfectedzone.common.zombie.abilities.Hearing;
import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!event.getEntityLiving().getLevel().isClientSide()) {
            if (ZombieUtils.isTarget(event.getEntityLiving())) {
                Hearing.Utils.sound(Hearing.SoundType.BLOOD, (ServerLevel) event.getEntityLiving().getLevel(), event.getEntityLiving().blockPosition());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingAttackEvent event) {
        if (!event.getEntityLiving().getLevel().isClientSide()) {
            if (ZombieUtils.isTarget(event.getEntityLiving())) {
                Hearing.Utils.sound(Hearing.SoundType.BLOOD, (ServerLevel) event.getEntityLiving().getLevel(), event.getEntityLiving().blockPosition());
            }
        }
    }

    @SubscribeEvent
    public static void onProjectileHit(ProjectileImpactEvent event) {
        if (!event.getProjectile().getLevel().isClientSide()) {
            Hearing.Utils.sound(Hearing.SoundType.PROJECTILE, (ServerLevel) event.getProjectile().getLevel(), new BlockPos(event.getRayTraceResult().getLocation()));
        }
    }


    @SubscribeEvent
    public static void onUsingItem(LivingEntityUseItemEvent.Stop event) {
        if (!event.getEntityLiving().getLevel().isClientSide()) {
            if (ZombieUtils.isTarget(event.getEntityLiving())) {
                Hearing.Utils.sound(Hearing.SoundType.ACTION, (ServerLevel) event.getEntityLiving().getLevel(), event.getEntityLiving().blockPosition());
            }
        }
    }

    @SubscribeEvent
    public static void onUsingItem(LivingEntityUseItemEvent.Start event) {
        if (!event.getEntityLiving().getLevel().isClientSide()) {
            if (ZombieUtils.isTarget(event.getEntityLiving())) {
                Hearing.Utils.sound(Hearing.SoundType.ACTION, (ServerLevel) event.getEntityLiving().getLevel(), event.getEntityLiving().blockPosition());
            }
        }
    }


    @SubscribeEvent
    public static void onOpenChest(PlayerContainerEvent.Open event) {
        if (!event.getEntityLiving().getLevel().isClientSide()) {
            if (ZombieUtils.isTarget(event.getEntityLiving())) {
                Hearing.Utils.sound(Hearing.SoundType.ACTION, (ServerLevel) event.getEntityLiving().getLevel(), event.getEntityLiving().blockPosition());
            }
        }
    }

    @SubscribeEvent
    public static void onOpenCloseChest(PlayerContainerEvent.Close event) {
        if (!event.getEntityLiving().getLevel().isClientSide()) {
            if (ZombieUtils.isTarget(event.getEntityLiving())) {
                Hearing.Utils.sound(Hearing.SoundType.ACTION, (ServerLevel) event.getEntityLiving().getLevel(), event.getEntityLiving().blockPosition());
            }
        }
    }
}
