package com.aljun.uninfectedzone.common.zombie.abilities;

import com.aljun.uninfectedzone.api.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.api.zombie.abilities.ZombieAbilityInstance;
import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class Hearing extends ZombieAbility {

    public static final String KEY = "hearing";

    @Override
    protected ZombieAbilityInstance<? extends ZombieAbility> create(ZombieMainGoal mainGoal) {
        return new HearingInstance(this, mainGoal);
    }

    @Override
    protected void init(ZombieAbilityInstance<?> instance) {

    }

    @Override
    public String getKey() {
        return KEY;
    }

    public static class HearingInstance extends ZombieAbilityInstance<Hearing> {

        public HearingInstance(Hearing ability, ZombieMainGoal main) {
            super(ability, main);
        }

        @Override
        public void tick() {

        }

        public void hearSound(BlockPos blockPos, int raspy) {
            this.MAIN_GOAL.addInterestPos(blockPos, raspy);
        }
    }

    public static class Utils {
        private static double rangedDouble(double min, double aDouble, double max) {
            return Math.max(min, Math.min(aDouble, max));
        }

        public static void sound(SoundType soundType, ServerLevel level, BlockPos blockPos) {
            List<ZombieMainGoal> mainGoals = new ArrayList<>();
            level.getEntitiesOfClass(Mob.class, new AABB(blockPos).inflate(Math.min(512d, 512d * soundType.INTENSITY), Math.min(40d, 40d * soundType.INTENSITY), Math.min(512d, 512d * soundType.INTENSITY)), e -> {
                        ZombieMainGoal zombieMainGoal = ZombieUtils.getMainGoalOrAbsent(e);
                        if (zombieMainGoal != null) {
                            mainGoals.add(zombieMainGoal);
                        }
                        return false;
                    }
            );

            mainGoals.forEach(mainGoal -> {
                BlockPos pos = mainGoal.getZombie().blockPosition();
                double dis = blockPos.distSqr(pos);
                if (dis <= soundType.INTENSITY * ZombieUtils.getZombieHearingDistance(mainGoal.getZombie())) {
                    HearingInstance hearing = (HearingInstance) mainGoal.getAbilityInstanceOrAbsent(ZombieAbilities.HEARING.get());
                    if (hearing != null) {
                        hearing.hearSound(blockPos, soundType.RASPY);
                    }
                }
            });

        }
    }

    public static class SoundType {


        //僵尸能听到血液流动声音，这很核理
        public static final SoundType BLOOD = new SoundType("blood", 7, 2d);

        public static final SoundType EXPLOSION = new SoundType("explosion", 5, 4d);
        public static final SoundType BLOCK = new SoundType("block", 1, 1d);
        public static final SoundType ACTION = new SoundType("move", 6, 0.3d);
        public static final SoundType PROJECTILE = new SoundType("projectile", 5, 0.1d);

        public final String ID;
        public final int RASPY;
        public final double INTENSITY;

        private SoundType(String id, int raspy, double distanceModify) {
            this.ID = id;
            this.RASPY = raspy;
            this.INTENSITY = rangedDouble(0d, distanceModify, 5d);
        }

        private static double rangedDouble(double min, double aDouble, double max) {
            return Math.max(min, Math.min(aDouble, max));
        }

        public static SoundType custom(int raspy, double intensity) {
            return new SoundType("custom", raspy, intensity);
        }

    }
}
