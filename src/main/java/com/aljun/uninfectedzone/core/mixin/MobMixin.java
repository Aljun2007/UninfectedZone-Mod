package com.aljun.uninfectedzone.core.mixin;

import com.aljun.uninfectedzone.common.zombie.attribute.ZombieAttributes;
import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "createMobAttributes", at = @At("RETURN"))
    private static void createMobAttributesMixin(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        ZombieAttributes.buildSupplier(cir.getReturnValue());
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    public void registerGoalsMixin(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (ZombieUtils.isLoaded(mob)) {
            mob.goalSelector.removeAllGoals();
        }
    }
}
