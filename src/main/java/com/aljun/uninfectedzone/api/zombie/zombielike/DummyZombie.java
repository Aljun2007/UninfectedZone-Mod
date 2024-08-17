package com.aljun.uninfectedzone.api.zombie.zombielike;

import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.entity.Mob;

public class DummyZombie extends ZombieLike {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return new ZombieMainGoal(mob, this);
    }

    @Override
    protected void loadGoals(ZombieMainGoal zombieMainGoal) {
    }

    @Override
    protected void loadAbilities(ZombieMainGoal zombieMainGoal) {
    }


}
