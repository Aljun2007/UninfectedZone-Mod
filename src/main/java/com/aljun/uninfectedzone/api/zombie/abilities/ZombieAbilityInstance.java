package com.aljun.uninfectedzone.api.zombie.abilities;


import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;

public abstract class ZombieAbilityInstance<T extends ZombieAbility> implements ZombieMainGoal.BroadcastReceiver {
    public final T ABILITY;
    public final ZombieMainGoal MAIN_GOAL;

    public ZombieAbilityInstance(T ability, ZombieMainGoal main) {
        this.ABILITY = ability;
        this.MAIN_GOAL = main;
    }

    public boolean is(ZombieAbility ability) {
        return ability.equals(ABILITY);
    }

    public abstract void tick();

    public Mob getZombie() {
        return this.MAIN_GOAL.getZombie();
    }

    public CompoundTag saveAsTag() {
        return new CompoundTag();
    }

    public void loadFromTag(CompoundTag tag) {

    }
}