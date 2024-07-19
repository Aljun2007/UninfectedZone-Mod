package com.aljun.uninfectedzone.core.mixin;

import com.aljun.uninfectedzone.core.event.provider.UninfectedZoneEventFactory;
import com.aljun.uninfectedzone.core.event.provider.zombie.SetZombieConvertsInWaterEvent;
import com.aljun.uninfectedzone.core.event.provider.zombie.SetZombieSunSensitiveEvent;
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
        SetZombieSunSensitiveEvent event = UninfectedZoneEventFactory.onGettingZombieSunSensitive((Zombie) (Object) this, cir.getReturnValue());
        if (!event.isCanceled()) {
            if (event.isDirty()) {
                cir.setReturnValue(event.isSunSensitive());
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
}
