package com.aljun.uninfectedzone.common.zombie.goals;


import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class ZombieMeleeAttackGoal extends Goal {
    protected static final int KEY_TICK = 0;
    protected static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
    protected final ZombieMainGoal MAIN_GOAL;
    protected final Mob ZOMBIE;
    protected final int attackInterval = 20;
    protected double speedModifier = 1;
    protected boolean followingTargetEvenIfNotSeen;
    protected Path path;
    protected double pathedTargetX;
    protected double pathedTargetY;
    protected double pathedTargetZ;
    protected int ticksUntilNextPathRecalculation;
    protected int ticksUntilNextAttack;
    protected long lastCanUseCheck;
    protected int failedPathFindingPenalty = 0;
    protected boolean canPenalize = false;

    public ZombieMeleeAttackGoal(ZombieMainGoal mainGoal, Mob zombie) {
        this.ZOMBIE = zombie;
        this.MAIN_GOAL = mainGoal;
        this.followingTargetEvenIfNotSeen = true;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        long i = this.ZOMBIE.level.getGameTime();
        if (i - this.lastCanUseCheck < 20L) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.ZOMBIE.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (canPenalize) {
                    if (--this.ticksUntilNextPathRecalculation <= 0) {
                        this.path = this.ZOMBIE.getNavigation().createPath(livingentity, 0);
                        this.ticksUntilNextPathRecalculation = 4 + this.ZOMBIE.getRandom().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                }
                this.path = this.ZOMBIE.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.ZOMBIE.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                }
            }
        }
    }

    public boolean canContinueToUse() {
        LivingEntity livingentity = this.ZOMBIE.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.ZOMBIE.getNavigation().isDone();
        } else if (!this.ZOMBIE.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        } else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
        }
    }

    public void start() {
        this.ZOMBIE.getNavigation().moveTo(this.path, this.speedModifier);
        this.ZOMBIE.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    public void stop() {
        LivingEntity livingentity = this.ZOMBIE.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.ZOMBIE.setTarget(null);
        }

        this.ZOMBIE.setAggressive(false);
        this.ZOMBIE.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.ZOMBIE.getTarget();
        if (livingentity != null) {
            this.ZOMBIE.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            double d0 = this.ZOMBIE.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if (this.ZOMBIE.position().distanceToSqr(livingentity.position()) <= 25d) {
                this.ticksUntilNextPathRecalculation -= 2;
            }
            if ((this.followingTargetEvenIfNotSeen || this.ZOMBIE.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D || this.ZOMBIE.getRandom().nextFloat() < 0.05F)) {
                this.pathedTargetX = livingentity.getX();
                this.pathedTargetY = livingentity.getY();
                this.pathedTargetZ = livingentity.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.ZOMBIE.getRandom().nextInt(7);

                if (this.canPenalize) {
                    this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                    if (this.ZOMBIE.getNavigation().getPath() != null) {
                        Node finalPathPoint = this.ZOMBIE.getNavigation().getPath().getEndNode();
                        if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    } else {
                        failedPathFindingPenalty += 10;
                    }
                }

                if (d0 > 1024.0D) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256.0D) {
                    this.ticksUntilNextPathRecalculation += 5;
                }


                if (!this.ZOMBIE.getNavigation().moveTo(livingentity, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }


                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(livingentity, d0);
        }
    }

    protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
        double d0 = this.getAttackReachSqr(p_25557_);
        if (p_25558_ <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.ZOMBIE.swing(InteractionHand.MAIN_HAND);
            this.ZOMBIE.doHurtTarget(p_25557_);
        }

    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    protected double getAttackReachSqr(LivingEntity p_25556_) {
        return this.ZOMBIE.getBbWidth() * 2.0F * this.ZOMBIE.getBbWidth() * 2.0F + p_25556_.getBbWidth();
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected int getAttackInterval() {
        return this.adjustedTickDelay(20);
    }

}


