package com.aljun.uninfectedzone.core.zombie.like;

import com.aljun.uninfectedzone.core.zombie.awareness.ZombieAwareness;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import com.aljun.uninfectedzone.core.zombie.utils.ZombieMoveControl;
import net.minecraft.world.entity.Mob;

public class DummyZombie extends ZombieLike {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return new ZombieMainGoal(mob, DummyZombieAwareness::new, DummyZombieMoveControl::new);
    }

    @Override
    public void registerAbilities(ZombieMainGoal zombieMainGoal) {
    }

    @Override
    public void registerGoals(ZombieMainGoal zombieMainGoal) {
    }

    public static class DummyZombieMoveControl extends ZombieMoveControl {
        public DummyZombieMoveControl(ZombieMainGoal mainGoal) {
            super(mainGoal);
        }
    }

    public static class DummyZombieAwareness extends ZombieAwareness {
        public DummyZombieAwareness(ZombieMainGoal mainGoal) {
            super(mainGoal);
        }
    }
}
