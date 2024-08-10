package com.aljun.uninfectedzone.core.mixin;

import com.aljun.uninfectedzone.core.game.GameUtils;
import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Unique
    private static final Logger uninfectedZone_1_18_2$LOGGER = LogUtils.getLogger();

    @Shadow
    public abstract void move(MoverType p_19973_, Vec3 p_19974_);

    @Inject(method = "killed", at = @At("HEAD"))
    public void killedMixin(ServerLevel level, LivingEntity victim, CallbackInfo ci) {
        if (!GameUtils.isDisabled()) {

        }
    }

    @SuppressWarnings("all")
    @Inject(method = "saveWithoutId", at = @At(value = "HEAD"))
    public void saveWithoutIdMixin(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        try {
            Entity entity = (Entity) (Object) this;
            if (entity instanceof Mob mob) {
                ZombieMainGoal mainGoal = ZombieUtils.getMainGoalOrAbsent(mob);
                if (mainGoal != null) {
                    mainGoal.saveAbilitiesAsTag();
                }
            }
        } catch (Throwable e) {
            uninfectedZone_1_18_2$LOGGER.error("Save Abilities Failed : {}", e.toString());
        }
    }
}
