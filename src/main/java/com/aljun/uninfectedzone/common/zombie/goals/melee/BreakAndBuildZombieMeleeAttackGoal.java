package com.aljun.uninfectedzone.common.zombie.goals.melee;

import com.aljun.uninfectedzone.common.zombie.abilities.Breaking;
import com.aljun.uninfectedzone.common.zombie.abilities.PathConstructing;
import com.aljun.uninfectedzone.common.zombie.abilities.Placing;
import com.aljun.uninfectedzone.common.zombie.abilities.ZombieAbilities;
import com.aljun.uninfectedzone.core.utils.MathUtils;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PowderSnowCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class BreakAndBuildZombieMeleeAttackGoal extends Goal implements ZombieMainGoal.BroadcastReceiver {
    protected static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
    protected final ZombieMainGoal MAIN_GOAL;
    protected final Mob mob;
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
    protected Breaking.BreakingInstance breaking;
    protected Placing.PlacingInstance placing;
    protected PathConstructing.PathConstructingInstance pathConstructing;
    protected State state = State.MELEE;
    protected BlockPos buildTargetPos = null;
    protected PathConstructing.PathConstructingInstance.PathPack pathPack;
    protected BlockPos selfPos;
    private long lastSetMeleeTime = 0L;

    public BreakAndBuildZombieMeleeAttackGoal(ZombieMainGoal mainGoal, Mob zombie) {
        this.mob = zombie;
        this.MAIN_GOAL = mainGoal;
        this.followingTargetEvenIfNotSeen = true;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.breaking = (Breaking.BreakingInstance) mainGoal.getAbilityInstanceOrAbsent(ZombieAbilities.BREAKING.get());
        this.placing = (Placing.PlacingInstance) mainGoal.getAbilityInstanceOrAbsent(ZombieAbilities.PLACING.get());
        this.pathConstructing = (PathConstructing.PathConstructingInstance) mainGoal.getAbilityInstanceOrAbsent(ZombieAbilities.PATH_CONSTRUCTING.get());
    }

    @Override
    public void receiveBroadcast(ZombieMainGoal.BroadcastType broadcastType) {
        if (broadcastType.is(ZombieMainGoal.BroadcastType.HURT)) {
            if (this.mob.getLastDamageSource() != null) {
                if (this.mob.getLastDamageSource().getEntity() != null) {
                    if (this.mob.getLastDamageSource().getEntity() instanceof LivingEntity) {
                        if (this.state.is(State.BUILD)) {
                            this.setMelee();
                        }
                    }
                }
            }
        }
    }

    protected void setMelee() {
        this.state = State.MELEE;
        this.buildTargetPos = null;
        this.mob.getNavigation().stop();
        this.breaking.stop();
        this.lastSetMeleeTime = this.mob.getLevel().getGameTime();
    }

    public boolean canUse() {
        long i = this.mob.level.getGameTime();
        if (i - this.lastCanUseCheck < 20L) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (canPenalize) {
                    if (--this.ticksUntilNextPathRecalculation <= 0) {
                        this.path = this.mob.getNavigation().createPath(livingentity, 0);
                        this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                }
                this.path = this.mob.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                }
            }
        }
    }

    public boolean canContinueToUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.mob.getNavigation().isDone();
        } else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        } else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
        }
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
        this.mob.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    public void stop() {
        LivingEntity livingentity = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.mob.setTarget(null);
        }

        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            if (this.state.is(State.MELEE)) {
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if (this.mob.position().distanceToSqr(livingentity.position()) <= 25d) {
                    this.ticksUntilNextPathRecalculation -= 2;
                }
                if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 &&
                        (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D ||
                                livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D ||
                                this.mob.getRandom().nextFloat() < 0.05F)) {
                    this.pathedTargetX = livingentity.getX();
                    this.pathedTargetY = livingentity.getY();
                    this.pathedTargetZ = livingentity.getZ();
                    this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);

                    if (this.canPenalize) {
                        this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                        if (this.mob.getNavigation().getPath() != null) {
                            Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
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

                    Path path = this.mob.getNavigation().createPath(livingentity, 0);
                    boolean b1 = false;
                    if (path != null) {
                        b1 = this.mob.getNavigation().moveTo(path, this.speedModifier);
                        if (this.canPathConstruct()) {
                            Node finalPathPoint = path.getEndNode();
                            if (finalPathPoint != null) {
                                if (this.mob.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) <= 20 &&
                                        livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) > 9) {
                                    this.setBuild(livingentity.blockPosition());
                                } else if (livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) <= 2) {
                                    this.setBuild(livingentity.blockPosition());
                                }
                            } else {
                                this.setBuild(livingentity.blockPosition());
                            }
                        }
                    } else {
                        if (this.canPathConstruct()) {
                            this.setBuild(livingentity.blockPosition());
                        }
                    }
                    if (!b1) {
                        this.ticksUntilNextPathRecalculation += 15;
                    }


                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingentity, d0);
            }
            if (this.state.is(State.BUILD)) {
                if (this.buildTargetPos == null || !this.canPathConstruct()) {
                    this.setMelee();
                    return;
                }
                if (this.pathPack == null) {
                    if (this.mob.blockPosition().distSqr(this.selfPos) <= 4) {

                        this.pathPack = this.pathConstructing.create(this.selfPos, this.buildTargetPos);

                        Path path = this.mob.getNavigation().createPath(livingentity, 0);

                        if (path != null) {
                            Node finalNode = path.getEndNode();
                            if (finalNode != null) {
                                BlockPos pathEnd = finalNode.asBlockPos();
                                BlockPos buildEnd = pathPack.pathStructure().getEndPos(pathPack.horizontalDirection(), this.selfPos);

                                if ((Math.sqrt(pathEnd.distSqr(livingentity.blockPosition()) + 10) < Math.sqrt(buildEnd.distSqr(livingentity.blockPosition())))) {
                                    this.setMelee();
                                    this.mob.getNavigation().moveTo(path, this.speedModifier);
                                }
                            }
                        }

                        if (pathPack.pathStructure().is(PathConstructing.PathStructure.SITU)) {
                            this.setMelee();
                            return;
                        }


                        Path path1 = this.mob.getNavigation().createPath(this.selfPos, 0);
                        if (path1 != null) {
                            this.mob.getNavigation().moveTo(path1, this.speedModifier);
                        }
                    } else {
                        if (this.mob.getNavigation().isDone()) {
                            Path path1 = this.mob.getNavigation().createPath(this.selfPos, 0);
                            if (path1 != null) {
                                this.mob.getNavigation().moveTo(path1, this.speedModifier);
                            }
                        }
                    }
                }
                if (this.pathPack != null) {
                    int pathIndex = 0;
                    if (this.mob.blockPosition().distSqr(this.selfPos) >= 1) {
                        if (this.mob.getNavigation().isDone()) {
                            Path path1 = this.mob.getNavigation().createPath(this.selfPos, 0);
                            if (path1 != null) {
                                this.mob.getNavigation().moveTo(path1, this.speedModifier);
                            }
                        }
                    }

                    while (true) {

                        if (pathIndex > this.pathPack.pathStructure().maxIndex()) {
                            this.selfPos = this.pathPack.pathStructure().getEndPos(pathPack.horizontalDirection(), this.selfPos);
                            this.pathPack = null;
                            break;
                        }

                        BlockPos pos = this.pathPack.pathStructure().getPos(pathIndex, pathPack.horizontalDirection(), this.selfPos);

                        if (isPosLegal(pos)) {
                            this.setMelee();
                            return;
                        }
                        BlockState blockState = this.mob.level.getBlockState(pos);
                        PathConstructing.BlockType type = this.pathPack.pathStructure().getType(pathIndex);
                        if (!this.verify(pos, blockState, type)) {
                            if (!this.execute(pos, blockState, type)) {
                                this.setMelee();
                                return;
                            }
                            break;
                        }
                        pathIndex++;
                    }
                }
            }
        }
    }

    protected boolean canPathConstruct() {
        return this.pathConstructing != null && (this.canBreak() || this.canPlace());
    }

    protected void setBuild(BlockPos target) {
        if (-this.lastSetMeleeTime + this.mob.getLevel().getGameTime() >= 100L) {
            this.state = State.BUILD;
            this.buildTargetPos = target;
            this.selfPos = this.mob.blockPosition();
            this.mob.getNavigation().stop();
        }
    }

    protected void checkAndPerformAttack(LivingEntity livingEntity, double distance) {
        double d0 = this.getAttackReachSqr(livingEntity);
        if (distance <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(livingEntity);
        }
    }

    protected boolean isPosLegal(BlockPos pos) {
        return this.mob.getLevel().isOutsideBuildHeight(pos);
    }

    protected boolean verify(BlockPos blockPos, BlockState blockState, PathConstructing.@NotNull BlockType type) {
        if (type.is(PathConstructing.BlockType.EMPTY)) {
            return isEmpty(blockState);
        } else if (type.is(PathConstructing.BlockType.SOLID)) {
            return isSolid(blockPos, blockState);
        } else return false;
    }

    protected boolean execute(BlockPos blockPos, BlockState blockState, PathConstructing.@NotNull BlockType type) {
        if (type.is(PathConstructing.BlockType.SOLID)) {
            if (this.isEmpty(blockState)) {
                if (blockState.getBlock() instanceof PowderSnowCauldronBlock) {
                    return this.destroyBlock(blockPos);
                } else {
                    if (this.mob.blockPosition().equals(blockPos)) {
                        if (this.mob.isOnGround()) {
                            this.mob.getJumpControl().jump();
                            return true;
                        } else return !this.mob.isSwimming();
                    }
                    if (this.mob.distanceToSqr(MathUtils.blockPosToVec3(blockPos)) <= 9d) {
                        return this.placeBlock(blockPos);
                    } else if (this.mob.distanceToSqr(MathUtils.blockPosToVec3(blockPos)) > 25d) {
                        return false;
                    } else {
                        if (this.mob.blockPosition().distSqr(this.selfPos) >= 2) {
                            if (this.mob.getNavigation().isDone()) {
                                Path path1 = this.mob.getNavigation().createPath(this.selfPos, 0);
                                if (path1 != null) {
                                    this.mob.getNavigation().moveTo(path1, this.speedModifier);
                                } else {
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                }
            } else {
                return this.destroyBlock(blockPos);
            }
        } else if (type.is(PathConstructing.BlockType.EMPTY)) {
            return this.destroyBlock(blockPos);
        } else {
            this.setMelee();
        }
        return false;
    }

    protected boolean canBreak() {
        return this.breaking != null;
    }

    protected boolean canPlace() {
        return this.placing != null;
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    protected boolean isEmpty(BlockState blockState) {
        return (blockState.isAir()
                || !blockState.getFluidState().isEmpty())
                && !(blockState.getBlock() instanceof PowderSnowCauldronBlock);
    }

    protected boolean isSolid(BlockPos blockPos, BlockState blockState) {
        return Block.isShapeFullBlock(blockState.getCollisionShape(this.mob.getLevel(), blockPos));
    }

    protected boolean destroyBlock(BlockPos blockPos) {
        if (this.canBreak()) {
            if (this.breaking.isDone()) {
                return this.breaking.checkToStartBreak(blockPos, this::failBreak);
            }
            return true;
        } else return false;
    }

    protected boolean placeBlock(BlockPos blockPos) {
        if (this.canPlace()) {
            return this.placing.place(blockPos, getPlaceBlock());
        } else return false;
    }

    protected void failBreak() {
        this.setMelee();
    }

    private static BlockState getPlaceBlock() {
        return Blocks.DIRT.defaultBlockState();
    }

    protected double getAttackReachSqr(LivingEntity p_25556_) {
        return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + p_25556_.getBbWidth();
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

    protected enum State {
        MELEE(0), BUILD(1);
        final int ID;

        State(int i) {
            this.ID = i;
        }

        boolean is(State state) {
            return this.ID == state.ID;
        }
    }
}
