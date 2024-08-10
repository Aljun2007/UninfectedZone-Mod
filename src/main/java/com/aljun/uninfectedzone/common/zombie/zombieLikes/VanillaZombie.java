package com.aljun.uninfectedzone.common.zombie.zombieLikes;

import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.common.zombie.goals.melee.ZombieMeleeAttackGoal;
import com.aljun.uninfectedzone.core.zombie.awareness.ZombieAwareness;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.entity.Mob;

public class VanillaZombie extends ZombieLike {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return new ZombieMainGoal(mob, VanillaZombieAwareness::new, this);
    }

    @Override
    protected void registerAbilities(ZombieMainGoal zombieMainGoal) {

    }

    @Override
    protected void registerGoals(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.getZombie().goalSelector.addGoal(1, new ZombieMeleeAttackGoal(zombieMainGoal, zombieMainGoal.getZombie()));
    }


    public static class VanillaZombieAwareness extends ZombieAwareness {
        public VanillaZombieAwareness(ZombieMainGoal mainGoal) {
            super(mainGoal);
        }
    }
}
