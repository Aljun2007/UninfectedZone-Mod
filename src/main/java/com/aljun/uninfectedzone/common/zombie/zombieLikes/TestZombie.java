package com.aljun.uninfectedzone.common.zombie.zombieLikes;


import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.common.zombie.abilities.ZombieAbilities;
import com.aljun.uninfectedzone.common.zombie.goals.melee.BreakAndBuildZombieMeleeAttackGoal;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.entity.Mob;

public class TestZombie extends ZombieLike {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return new ZombieMainGoal(mob, VanillaZombie.VanillaZombieAwareness::new, this);
    }

    @Override
    protected void registerAbilities(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.addAbility(ZombieAbilities.BREAKING.get());
        zombieMainGoal.addAbility(ZombieAbilities.PLACING.get());
        zombieMainGoal.addAbility(ZombieAbilities.PATH_CONSTRUCTING.get());
    }

    @Override
    protected void registerGoals(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.getZombie().goalSelector.addGoal(1, new BreakAndBuildZombieMeleeAttackGoal(zombieMainGoal, zombieMainGoal.getZombie()));
    }

}
