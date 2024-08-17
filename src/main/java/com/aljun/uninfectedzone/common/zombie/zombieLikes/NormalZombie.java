package com.aljun.uninfectedzone.common.zombie.zombieLikes;

import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.common.zombie.abilities.ZombieAbilities;
import com.aljun.uninfectedzone.common.zombie.goals.melee.ZombieMeleeAttackGoal;
import com.aljun.uninfectedzone.common.zombie.goals.target.ZombieTargetChoosingGoal;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;

public class NormalZombie extends ZombieLike {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return new ZombieMainGoal(mob, this);
    }

    @Override
    protected void loadGoals(ZombieMainGoal zombieMainGoal) {
        this.registerAttackingGoal(zombieMainGoal);
        this.registerBehavingGoals(zombieMainGoal);
        this.registerTargetChoosingGoal(zombieMainGoal);
    }

    @Override
    protected void loadAbilities(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.addAbility(ZombieAbilities.HEARING.get());
    }

    protected void registerAttackingGoal(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.getZombie().goalSelector.addGoal(1, new ZombieMeleeAttackGoal(zombieMainGoal, zombieMainGoal.getZombie()));
    }

    protected void registerBehavingGoals(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.getZombie().goalSelector.addGoal(8, new LookAtPlayerGoal(zombieMainGoal.getZombie(), Player.class, 8.0F));
        zombieMainGoal.getZombie().goalSelector.addGoal(8, new RandomLookAroundGoal(zombieMainGoal.getZombie()));

    }

    protected void registerTargetChoosingGoal(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.getZombie().goalSelector.addGoal(1, new ZombieTargetChoosingGoal(zombieMainGoal));

    }
}
