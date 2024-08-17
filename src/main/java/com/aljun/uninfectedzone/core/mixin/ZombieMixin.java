package com.aljun.uninfectedzone.core.mixin;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.api.event.zombie.SetZombieConvertsInWaterEvent;
import com.aljun.uninfectedzone.api.event.zombie.SetZombieSunSensitiveEvent;
import com.aljun.uninfectedzone.common.sound.UninfectedZoneSounds;
import com.aljun.uninfectedzone.core.event.provider.UninfectedZoneEventFactory;
import com.aljun.uninfectedzone.core.utils.TagUtils;
import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public class ZombieMixin extends Mob {
    protected ZombieMixin(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Inject(method = "isSunSensitive", at = @At("RETURN"), cancellable = true)
    public void isSunSensitiveMixin(CallbackInfoReturnable<Boolean> cir) {
        Zombie zombie = (Zombie) (Object) this;
        if (!zombie.getLevel().isClientSide()) {
            boolean b = cir.getReturnValue() && TagUtils.fastRead(TagUtils.getRoot(zombie), ZombieUtils.SUN_SENSITIVE);
            if (b != cir.getReturnValue()) {
                cir.setReturnValue(b);
            }
            SetZombieSunSensitiveEvent event = UninfectedZoneEventFactory.onGettingZombieSunSensitive(zombie, cir.getReturnValue());
            if (!event.isCanceled()) {
                if (event.isDirty()) {
                    cir.setReturnValue(event.isSunSensitive());
                }
            }
        }
    }

    @Inject(method = "convertsInWater", at = @At("RETURN"), cancellable = true)
    public void convertsInWaterMixin(CallbackInfoReturnable<Boolean> cir) {
        SetZombieConvertsInWaterEvent event = UninfectedZoneEventFactory.onGettingZombieConvertsInWater((Zombie) (Object) this, cir.getReturnValue());
        if (!event.isCanceled()) {
            if (event.isDirty()) {
                cir.setReturnValue(event.convertsInWater());
            }
        }
    }

    @Inject(method = "getHurtSound", at = @At("RETURN"), cancellable = true)
    protected void getHurtSoundMixin(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
        if (UninfectedZone.useZombiePackSounds) {
            cir.setReturnValue(UninfectedZoneSounds.CUSTOM_ZOMBIE_HURT.get());
        }
    }

    @Inject(method = "getDeathSound", at = @At("RETURN"), cancellable = true)
    protected void getDeathSoundMixin(CallbackInfoReturnable<SoundEvent> cir) {
        if (UninfectedZone.useZombiePackSounds) {
            cir.setReturnValue(UninfectedZoneSounds.CUSTOM_ZOMBIE_DEATH.get());
        }
    }

    @Inject(method = "getAmbientSound", at = @At("RETURN"), cancellable = true)
    protected void getAmbientSoundMixin(CallbackInfoReturnable<SoundEvent> cir) {
        if (UninfectedZone.useZombiePackSounds) {
            cir.setReturnValue(UninfectedZoneSounds.CUSTOM_ZOMBIE_AMBIENT.get());
        }
    }

    @Inject(method = "getStepSound", at = @At("RETURN"), cancellable = true)
    protected void getStepSoundMixin(CallbackInfoReturnable<SoundEvent> cir) {
        if (UninfectedZone.useZombiePackSounds) {
            cir.setReturnValue(UninfectedZoneSounds.CUSTOM_ZOMBIE_STEP.get());
        }
    }
}
