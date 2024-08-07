package com.aljun.uninfectedzone.common.zombie.abilities;

import com.aljun.uninfectedzone.api.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.api.zombie.abilities.ZombieAbilityInstance;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class Placing extends ZombieAbility {
    public static final String KEY = "placing";

    @Override
    protected ZombieAbilityInstance<? extends ZombieAbility> create(ZombieMainGoal mainGoal) {
        return new PlacingInstance(this, mainGoal);
    }

    @Override
    protected void init(ZombieAbilityInstance<?> instance) {

    }

    @Override
    public String getKey() {
        return KEY;
    }

    public static class PlacingInstance extends ZombieAbilityInstance<Placing> {

        private final Mob mob;
        private long lastPlaceTime = 0L;

        public PlacingInstance(Placing ability, ZombieMainGoal main) {
            super(ability, main);
            this.mob = this.getZombie();
        }

        public boolean place(BlockPos blockPos, BlockState blockState) {
            if (this.lastPlaceTime + 20L >= this.mob.getLevel().getGameTime()) {
                return true;
            }
            if (this.checkState(blockState) && this.checkPos(blockPos)) {
                this.succeedPlace(blockPos, blockState);
                return true;
            } else {
                return false;
            }
        }

        private boolean checkState(BlockState blockState) {
            return (!blockState.isAir() && blockState.getFluidState().isEmpty());
        }

        private boolean checkPos(BlockPos pos) {
            if (this.mob.getOnPos().equals(pos)) return false;
            if (!mob.getLevel().isOutsideBuildHeight(pos)) return false;
            if (mob.blockPosition().distSqr(pos) <= 16d) return false;
            return (mob.getLevel().getBlockState(pos).isAir() || !mob.getLevel().getBlockState(pos).getFluidState().isEmpty());
        }

        private void succeedPlace(BlockPos blockPos, BlockState blockState) {
            ServerLevel level = (ServerLevel) this.getZombie().level;
            level.setBlock(blockPos, blockState, 3);
            SoundType soundType = blockState.getSoundType();
            this.mob.getLevel().playSound(null, blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS,
                    (soundType.getVolume() + 1.0F) / 8.0F, soundType.getPitch() * 0.5F);

            this.lastPlaceTime = this.mob.getLevel().getGameTime();
        }


        @Override
        public void tick() {
        }
    }
}
