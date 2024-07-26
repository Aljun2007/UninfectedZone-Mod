package com.aljun.uninfectedzone.deafult.zombie.zombieLikes;


import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import com.aljun.uninfectedzone.deafult.zombie.abilities.ZombieAbilities;
import com.aljun.uninfectedzone.deafult.zombie.goals.BreakAndBuildZombieMeleeAttackGoal;
import net.minecraft.world.entity.Mob;

public class TestZombie extends ZombieLike {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return new ZombieMainGoal(mob, VanillaZombie.VanillaZombieAwareness::new, VanillaZombie.LandZombieMoveControl::new);
    }

    @Override
    protected void registerAbilities(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.addAbility(ZombieAbilities.BREAKING.get());
    }

    @Override
    protected void registerGoals(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.getZombie().goalSelector.addGoal(1, new BreakAndBuildZombieMeleeAttackGoal(zombieMainGoal, zombieMainGoal.getZombie()));
    }

}
