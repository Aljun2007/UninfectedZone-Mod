package com.aljun.uninfectedzone.core.utils;

import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;

public class ZombieUtils {

    public static boolean canBeAttack(LivingEntity target) {
        return true;
    }

    @Nullable
    public ZombieMainGoal getMainGoalOrAbsent(Mob mob) {
        final ZombieMainGoal[] result = {null};
        mob.goalSelector.getAvailableGoals().forEach((goal) -> {
                    if (goal.getGoal().getClass().equals(ZombieMainGoal.class)) {
                        result[0] = (ZombieMainGoal) goal.getGoal();
                    }
                }
        );
        return result[0];
    }
}
