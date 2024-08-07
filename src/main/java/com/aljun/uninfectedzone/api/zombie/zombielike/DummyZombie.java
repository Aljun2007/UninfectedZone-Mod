package com.aljun.uninfectedzone.api.zombie.zombielike;

import com.aljun.uninfectedzone.core.zombie.awareness.ZombieAwareness;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.entity.Mob;

public class DummyZombie extends ZombieLike {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return new ZombieMainGoal(mob, DummyZombieAwareness::new, this);
    }

    @Override
    protected void registerAbilities(ZombieMainGoal zombieMainGoal) {
    }

    @Override
    protected void registerGoals(ZombieMainGoal zombieMainGoal) {
    }

    public static class DummyZombieAwareness extends ZombieAwareness {
        public DummyZombieAwareness(ZombieMainGoal mainGoal) {
            super(mainGoal);
        }
    }
}
