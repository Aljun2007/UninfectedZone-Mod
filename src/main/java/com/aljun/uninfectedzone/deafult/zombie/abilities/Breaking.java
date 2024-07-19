package com.aljun.uninfectedzone.deafult.zombie.abilities;

import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbilityInstance;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;

public class Breaking extends ZombieAbility {
    public static final String KEY = "breaking";

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

    public static class BreakingInstance extends ZombieAbilityInstance<Breaking> {

        public BreakingInstance(Breaking ability, ZombieMainGoal main) {
            super(ability, main);
        }

        @Override
        public void tick() {

        }
    }

}
