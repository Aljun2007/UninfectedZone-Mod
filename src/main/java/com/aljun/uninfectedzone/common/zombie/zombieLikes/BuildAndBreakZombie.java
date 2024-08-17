package com.aljun.uninfectedzone.common.zombie.zombieLikes;


import com.aljun.uninfectedzone.common.zombie.abilities.ZombieAbilities;
import com.aljun.uninfectedzone.common.zombie.goals.melee.BreakAndBuildZombieMeleeAttackGoal;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.entity.Mob;

public class BuildAndBreakZombie extends NormalZombie {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return super.createMainGoal(mob);
    }

    @Override
    protected void loadGoals(ZombieMainGoal zombieMainGoal) {
        super.loadAbilities(zombieMainGoal);
    }

    @Override
    protected void loadAbilities(ZombieMainGoal zombieMainGoal) {
        super.loadAbilities(zombieMainGoal);
        zombieMainGoal.addAbility(ZombieAbilities.BREAKING.get());
        zombieMainGoal.addAbility(ZombieAbilities.PLACING.get());
        zombieMainGoal.addAbility(ZombieAbilities.PATH_CONSTRUCTING.get());
    }

    @Override
    protected void registerAttackingGoal(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.getZombie().goalSelector.addGoal(1, new BreakAndBuildZombieMeleeAttackGoal(zombieMainGoal, zombieMainGoal.getZombie()));
    }
}
