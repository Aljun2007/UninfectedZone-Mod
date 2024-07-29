package com.aljun.uninfectedzone.core.mixin;

import com.aljun.uninfectedzone.core.game.GameUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "killed", at = @At("HEAD"))
    public void killedMixin(ServerLevel level, LivingEntity victim, CallbackInfo ci) {
        if (!GameUtils.isUninfectedZoneInfectionDisabled()) {

        }
    }
}
