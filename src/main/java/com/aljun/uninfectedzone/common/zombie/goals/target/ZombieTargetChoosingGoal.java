package com.aljun.uninfectedzone.common.zombie.goals.target;

import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

public class ZombieTargetChoosingGoal extends Goal {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final TargetingConditions conditionBySmell;
    private final ServerLevel level;
    private final Mob mob;
    private final int randomInt1;
    private final ZombieMainGoal mainGoal;


    public ZombieTargetChoosingGoal(ZombieMainGoal zombieMainGoal) {
        this.mainGoal = zombieMainGoal;
        this.mob = zombieMainGoal.getZombie();
        this.level = (ServerLevel) zombieMainGoal.getZombie().getLevel();
        this.randomInt1 = this.mob.hashCode() % 10;
        this.conditionBySmell = TargetingConditions.forCombat().range(ZombieUtils.getZombieSmellingDistance(this.mob)).ignoreLineOfSight();
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        LivingEntity foe = this.mob.getLastHurtMob();
        if (this.mob.getTarget() != foe && foe != null) {
            if (ZombieUtils.isTargetLegal(foe)) {
                this.mob.setTarget(foe);
            }
        } else if (foe == null) {
            if (this.level.getGameTime() % 10 == this.randomInt1) {
                LivingEntity entity = this.getAttackableEntity();
                if (entity != null && entity != target) {
                    if (target == null) {
                        this.mob.setTarget(entity);
                    } else {
                        if (target instanceof Player player) {
                            if (this.mob.distanceToSqr(player) <= 100) {
                                this.mob.setTarget(player);
                            }
                        }
                    }
                }

            }
        }
        if (!ZombieUtils.isTargetLegal(target)) {
            if (!ZombieUtils.canZombieFollow(this.mob, target)) {
                this.mob.setTarget(null);
            }
        }
    }

    private LivingEntity getAttackableEntity() {
        return this.getNearestAttackableTargetBySmelling();
    }

    private LivingEntity getNearestAttackableTargetBySmelling() {
        return this.mob.level.getNearestEntity(this.mob.level.getEntitiesOfClass(LivingEntity.class, this.getTargetSearchArea(ZombieUtils.getZombieSmellingDistance(this.mob)), p_148152_ -> true), conditionBySmell, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
    }

    protected AABB getTargetSearchArea(double distance) {
        return this.mob.getBoundingBox().inflate(distance, Math.min(distance / 5, 40), distance);
    }

}
