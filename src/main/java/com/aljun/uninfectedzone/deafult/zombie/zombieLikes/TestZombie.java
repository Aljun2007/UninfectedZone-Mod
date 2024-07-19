package com.aljun.uninfectedzone.deafult.zombie.zombieLikes;


import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import com.aljun.uninfectedzone.deafult.zombie.goals.BreakAndBuildZombieMeleeAttackGoal;
import net.minecraft.world.entity.Mob;

public class TestZombie extends ZombieLike {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return new ZombieMainGoal(mob, VanillaZombie.VanillaZombieAwareness::new, VanillaZombie.LandZombieMoveControl::new);
    }

    @Override
    public void registerAbilities(ZombieMainGoal zombieMainGoal) {

    }

    @Override
    public void registerGoals(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.getZombie().goalSelector.addGoal(1, new BreakAndBuildZombieMeleeAttackGoal(zombieMainGoal, zombieMainGoal.getZombie()));
    }

}
