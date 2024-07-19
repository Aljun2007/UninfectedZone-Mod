package com.aljun.uninfectedzone.deafult.zombie.abilities;

import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbilityInstance;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;

public class PathConstructing extends ZombieAbility {
    public static String KEY = "path_constructing";

    @Override
    protected ZombieAbilityInstance<? extends ZombieAbility> create(ZombieMainGoal mainGoal) {
        return null;
    }

    @Override
    protected void init(ZombieAbilityInstance<?> instance) {

    }


    @Override
    public String getKey() {
        return KEY;
    }


}
