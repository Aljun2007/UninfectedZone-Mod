package com.aljun.uninfectedzone.core.zombie.abilities;

import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ZombieAbility extends ForgeRegistryEntry<ZombieAbility> {
    public ZombieAbilityInstance<?> createInstance(ZombieMainGoal mainGoal) {
        ZombieAbilityInstance<?> instance = this.create(mainGoal);
        this.init(instance);
        return instance;
    }

    protected abstract ZombieAbilityInstance<? extends ZombieAbility> create(ZombieMainGoal mainGoal);

    protected abstract void init(ZombieAbilityInstance<?> instance);


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ZombieAbility) {
            return ((ZombieAbility) obj).getKey().equals(this.getKey());
        } else {
            return false;
        }
    }

    public abstract String getKey();
}
