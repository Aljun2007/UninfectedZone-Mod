package com.aljun.uninfectedzone.common.minecraft.entity;


import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.common.sound.UninfectedZoneSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CustomZombie extends Zombie {
    private static final EntityDataAccessor<Boolean> DATA_SLIM = SynchedEntityData.defineId(CustomZombie.class, EntityDataSerializers.BOOLEAN);


    protected CustomZombie(EntityType<? extends CustomZombie> entityType, Level level, boolean slim) {
        super(entityType, level);
        this.getEntityData().define(DATA_SLIM, slim);
    }

    public static CustomZombie createSlim(EntityType<? extends CustomZombie> entityType, Level level) {
        return new CustomZombie(entityType, level, true);
    }

    public static CustomZombie createDefault(EntityType<? extends CustomZombie> entityType, Level level) {
        return new CustomZombie(entityType, level, false);
    }

    public boolean isSlim() {
        return this.getEntityData().get(DATA_SLIM);
    }

    protected SoundEvent getAmbientSound() {
        return UninfectedZone.useZombiePackSounds ? UninfectedZoneSounds.CUSTOM_ZOMBIE_AMBIENT.get() : SoundEvents.ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return UninfectedZone.useZombiePackSounds ? UninfectedZoneSounds.CUSTOM_ZOMBIE_HURT.get() : SoundEvents.ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return UninfectedZone.useZombiePackSounds ? UninfectedZoneSounds.CUSTOM_ZOMBIE_DEATH.get() : SoundEvents.ZOMBIE_DEATH;
    }

    protected @NotNull SoundEvent getStepSound() {
        return UninfectedZone.useZombiePackSounds ? UninfectedZoneSounds.CUSTOM_ZOMBIE_STEP.get() : SoundEvents.ZOMBIE_STEP;
    }


}
