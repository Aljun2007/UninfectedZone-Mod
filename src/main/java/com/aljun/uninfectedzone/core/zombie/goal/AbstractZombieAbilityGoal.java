package com.aljun.uninfectedzone.core.zombie.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class AbstractZombieAbilityGoal extends Goal {
    protected final ZombieMainGoal mainGoal;
    protected final Mob zombie;

    public AbstractZombieAbilityGoal(ZombieMainGoal mainGoal, Mob zombie) {
        this.mainGoal = mainGoal;
        this.zombie = zombie;
    }

    @Override
    public boolean canUse() {
        return false;
    }
}
