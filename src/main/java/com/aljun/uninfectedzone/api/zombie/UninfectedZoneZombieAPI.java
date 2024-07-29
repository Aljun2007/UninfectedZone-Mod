package com.aljun.uninfectedzone.api.zombie;

import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class UninfectedZoneZombieAPI {
    @Nullable
    public static ZombieMainGoal getMainGoalOrAbsent(Mob mob) {
        return ZombieUtils.getMainGoalOrAbsent(mob);
    }

}
