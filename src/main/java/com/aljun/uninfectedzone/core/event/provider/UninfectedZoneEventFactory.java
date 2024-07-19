package com.aljun.uninfectedzone.core.event.provider;

import com.aljun.uninfectedzone.core.event.provider.world.MobMaxCountEvent;
import com.aljun.uninfectedzone.core.event.provider.zombie.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.common.MinecraftForge;

public class UninfectedZoneEventFactory {
    public static MobMaxCountEvent onGettingMobMaxCount(MobCategory mobCategory) {
        MobMaxCountEvent event = new MobMaxCountEvent(mobCategory);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static SetZombieSunSensitiveEvent onGettingZombieSunSensitive(Zombie zombie, boolean defaultSunSensitive) {
        SetZombieSunSensitiveEvent event = new SetZombieSunSensitiveEvent(zombie, defaultSunSensitive);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static SetZombieConvertsInWaterEvent onGettingZombieConvertsInWater(Zombie zombie, boolean defaultConvertsInWater) {
        SetZombieConvertsInWaterEvent event = new SetZombieConvertsInWaterEvent(zombie, defaultConvertsInWater);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static LivingInfectionConvertEvent onLivingInfectionConvert(LivingEntity income, Mob outcome) {
        LivingInfectionConvertEvent event = new LivingInfectionConvertEvent(income, outcome);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static ZombieLoadEvent onZombieLoad(Mob mob) {
        ZombieLoadEvent event = new ZombieLoadEvent(mob);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static ZombieSpawnEvent onZombieSpawn(Mob mob) {
        ZombieSpawnEvent event = new ZombieSpawnEvent(mob);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }
}