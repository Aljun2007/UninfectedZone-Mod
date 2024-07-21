package com.aljun.uninfectedzone.core.mixin;

import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "registerGoals", at = @At("TAIL"))
    public void registerGoalsMixin(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (ZombieUtils.hasLoadedIfAbsentMainGoal(mob)) {
            mob.goalSelector.removeAllGoals();
        }
    }
}
