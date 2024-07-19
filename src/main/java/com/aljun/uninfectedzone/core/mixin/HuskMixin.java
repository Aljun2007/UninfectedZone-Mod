package com.aljun.uninfectedzone.core.mixin;

import com.aljun.uninfectedzone.core.event.provider.UninfectedZoneEventFactory;
import com.aljun.uninfectedzone.core.event.provider.zombie.SetZombieConvertsInWaterEvent;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Husk.class)
public class HuskMixin {
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
