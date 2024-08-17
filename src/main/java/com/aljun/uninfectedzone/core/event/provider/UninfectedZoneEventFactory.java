package com.aljun.uninfectedzone.core.event.provider;

import com.aljun.uninfectedzone.api.event.world.MobMaxCountEvent;
import com.aljun.uninfectedzone.api.event.zombie.*;
import com.aljun.uninfectedzone.core.game.mode.GameMode;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
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

    public static ZombieAttributeModifyEvent onModifyingAttributes(GameMode gameMode, Mob mob, double defaultValue, Attribute attribute) {
        ZombieAttributeModifyEvent event = new ZombieAttributeModifyEvent(gameMode, mob, defaultValue, attribute);
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

    public static void onZombieLoad(Mob mob) {
        ZombieLoadEvent event = new ZombieLoadEvent(mob);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static void onZombieSpawn(Mob mob) {
        ZombieSpawnEvent event = new ZombieSpawnEvent(mob);
        MinecraftForge.EVENT_BUS.post(event);
    }


}
