package com.aljun.uninfectedzone.deafult.zombie.abilities;

import com.aljun.uninfectedzone.core.utils.MathUtils;
import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbilityInstance;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Breaking extends ZombieAbility {
    public static final String KEY = "breaking";

    @Override
    protected ZombieAbilityInstance<? extends ZombieAbility> create(ZombieMainGoal mainGoal) {
        return null;
    }

    @Override
    protected void init(ZombieAbilityInstance<?> instance) {

    }

    @Override
    public String getKey() {
        return KEY;
    }

    public static class BreakingInstance extends ZombieAbilityInstance<Breaking> {

        public static final List<Block> BLACK_LIST = new ArrayList<>();

        static {
            BLACK_LIST.add(Blocks.BEDROCK);
            BLACK_LIST.add(Blocks.END_PORTAL_FRAME);
            BLACK_LIST.add(Blocks.END_PORTAL);
            BLACK_LIST.add(Blocks.NETHER_PORTAL);
        }

        private final Mob mob;
        private float progress = 0;
        private BlockState state = Blocks.AIR.defaultBlockState();
        private BlockPos pos = new BlockPos(0, 0, 0);
        private boolean isDone = true;
        private ServerLevel level;
        private long startTime = 0L;

        public BreakingInstance(Breaking ability, ZombieMainGoal main) {
            super(ability, main);
            this.mob = main.getZombie();
        }

        public boolean checkToStartBreak(BlockPos pos) {
            if (!canBreakBlockFromGameRules()) {
                return false;
            }

            if (canStart(pos, this.mob.getLevel().getBlockState(pos))) {
                this.startBreak(pos);
                return true;
            }
            return false;
        }

        private static boolean canBreakBlockFromGameRules() {
            return true;
        }

        private boolean canStart(BlockPos pos, BlockState state) {
            return isPositionCorrect(this.mob, pos)
                    && this.mob.isAlive()
                    && isStateCorrect(state)
                    && this.isDone();
        }

        private void startBreak(BlockPos pos) {
            this.pos = pos;
            this.level = (ServerLevel) this.mob.getLevel();
            this.state = this.level.getBlockState(pos);
            this.progress = 0f;
            this.isDone = false;
            this.startTime = this.mob.getLevel().getGameTime();
            if (!this.mob.isSwimming()) {
                this.mob.swing(InteractionHand.MAIN_HAND);
            }
        }

        private static boolean isPositionCorrect(Mob mob, BlockPos pos) {
            return mob.getLevel().getBlockState(pos).canEntityDestroy(mob.getLevel(), pos, mob)
                    && !mob.getLevel().isOutsideBuildHeight(pos)
                    && mob.getEyePosition().distanceTo(MathUtils.blockPosToVec3(pos)) <= 3d;
        }

        private static boolean isStateCorrect(BlockState state) {

            Block block2 = state.getBlock();

            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block2);
            String id = "";
            if (key != null) {
                id = key.toString();
            }

            boolean canBreak = !BLACK_LIST.contains(block2);

            if (block2 instanceof LiquidBlock || (!(block2 instanceof PowderSnowCauldronBlock)
                    && block2 instanceof AbstractCauldronBlock)) {
                canBreak = false;
            } else if (block2 instanceof GameMasterBlock) {
                canBreak = false;
            } else {
                canBreak = canBreak && !(state.isAir());
            }

            return canBreak;
        }

        public boolean isDone() {
            return isDone;
        }

        @Override
        public void tick() {

            if (this.isDone()) return;

            if (canContinue(this.pos)) {
                //暂且使用原版攻击目标
                if (this.mob.getTarget() == null) {
                    this.mob.getLookControl().setLookAt(MathUtils.blockPosToVec3(pos));
                }

                this.state = this.level.getBlockState(this.pos);
                this.progress += getBreakProgress(this.state, this.mob, this.pos);
                if (this.progress >= 1f) {
                    this.succeedBreak();
                } else {
                    this.level.destroyBlockProgress(this.mob.getId(), this.pos, (int) (progress * 10f) - 1);
                    if (!this.mob.swinging) {
                        this.mob.swing(InteractionHand.MAIN_HAND);
                    }
                    if ((this.mob.getLevel().getGameTime() - this.startTime) % 4L == 0) {
                        SoundType soundType = this.state.getSoundType();
                        this.mob.getLevel().playSound(null, this.pos, soundType.getHitSound(), SoundSource.BLOCKS,
                                (soundType.getVolume() + 1.0F) / 8.0F, soundType.getPitch() * 0.5F);
                    }
                }
            } else {
                this.failBreak();
            }
        }
        // Simple Tools

        private boolean canContinue(BlockPos pos) {
            return isPositionCorrect(this.mob, pos)
                    && this.mob.isAlive()
                    && isStateCorrect(state);
        }

        //从原版抄来的
        private static float getBreakProgress(BlockState state, Mob mob, BlockPos pos) {

            ItemStack stack = mob.getItemBySlot(EquipmentSlot.MAINHAND);

            float f = state.getDestroySpeed(mob.getLevel(), pos);
            if (f == -1.0F) {
                return 0;
            } else {
                boolean a = !state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state);
                int i = a ? 30 : 100;
                return (getDigSpeed(state, pos, stack, mob) / f / (float) i);
            }
        }

        private void succeedBreak() {
            if (!this.isDone()) {
                if (this.canPerformDestroy(this.pos, this.state)) {
                    destroyBlock(this.mob, this.pos);
                }
                this.level.destroyBlockProgress(this.mob.getId(), pos, -1);
                this.isDone = true;
            }
        }


        private void failBreak() {
            if (!this.isDone()) {
                this.level.destroyBlockProgress(this.mob.getId(), pos, -1);
                this.isDone = true;
            }
        }

        //从原版抄来的
        public static float getDigSpeed(BlockState state, @Nullable BlockPos pos, ItemStack stack, Mob mob) {
            float f = stack.getDestroySpeed(state);
            if (f > 1.0F) {
                int i = EnchantmentHelper.getBlockEfficiency(mob);
                if (i > 0 && !stack.isEmpty()) {
                    f += (float) (i * i + 1);
                }
            }

            if (MobEffectUtil.hasDigSpeed(mob)) {
                f *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(mob) + 1) * 0.2F;
            }

            if (mob.hasEffect(MobEffects.DIG_SLOWDOWN)) {
                float f1 = switch (Objects.requireNonNull(mob.getEffect(MobEffects.DIG_SLOWDOWN)).getAmplifier()) {
                    case 0 -> 0.3F;
                    case 1 -> 0.09F;
                    case 2 -> 0.0027F;
                    default -> 8.1E-4F;
                };

                f *= f1;
            }

            if (!mob.getType().equals(EntityType.DROWNED) && (mob.isEyeInFluid(FluidTags.WATER)) && !EnchantmentHelper.hasAquaAffinity(mob)) {
                f /= 5.0F;
            }

            if (!mob.isOnGround()) {
                f /= 5.0F;
            }

            return f;
        }

        private boolean canPerformDestroy(BlockPos pos, BlockState state) {
            return isPositionCorrect(this.mob, pos)
                    && isStateCorrect(state) && ForgeHooks.canEntityDestroy(this.mob.getLevel(), pos, this.mob);
        }

        //模仿玩家破坏
        private static void destroyBlock(Mob mob, BlockPos pos) {
            ItemStack stack = mob.getItemBySlot(EquipmentSlot.MAINHAND);
            BlockState state = mob.getLevel().getBlockState(pos);
            Level level = mob.getLevel();
            dropResources(level, mob, pos, state, level.getBlockEntity(pos), stack);
            level.destroyBlock(pos, false, mob);

        }

        //从原版抄来的
        private static void dropResources(Level level, Mob mob, BlockPos pos, BlockState state,
                                          @Nullable BlockEntity blockEntity, ItemStack stack) {
            if (!state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state)) {
                Block.dropResources(state, level, pos, blockEntity, mob, stack);
            }
        }

        public void stop() {
            this.failBreak();
        }

    }

}
