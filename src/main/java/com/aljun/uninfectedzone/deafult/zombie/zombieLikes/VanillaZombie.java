package com.aljun.uninfectedzone.deafult.zombie.zombieLikes;

import com.aljun.uninfectedzone.core.zombie.awareness.ZombieAwareness;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import com.aljun.uninfectedzone.core.zombie.utils.ZombieMoveControl;
import com.aljun.uninfectedzone.deafult.zombie.goals.ZombieMeleeAttackGoal;
import net.minecraft.world.entity.Mob;

public class VanillaZombie extends ZombieLike {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return new ZombieMainGoal(mob, VanillaZombieAwareness::new, LandZombieMoveControl::new);
    }

    @Override
    public void registerAbilities(ZombieMainGoal zombieMainGoal) {

    }

    @Override
    public void registerGoals(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.getZombie().goalSelector.addGoal(1, new ZombieMeleeAttackGoal(zombieMainGoal, zombieMainGoal.getZombie()));
    }

    public static class LandZombieMoveControl extends ZombieMoveControl {
        public LandZombieMoveControl(ZombieMainGoal mainGoal) {
            super(mainGoal);
        }
    }

    public static class VanillaZombieAwareness extends ZombieAwareness {
        public VanillaZombieAwareness(ZombieMainGoal mainGoal) {
            super(mainGoal);
        }
    }
}
