package com.aljun.uninfectedzone.core.zombie.awareness;


import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;

import java.util.ArrayList;
import java.util.List;

public abstract class ZombieAwareness {
    protected final ZombieMainGoal mainGoal;
    protected List<ZombieMemory> zombieMemories = new ArrayList<>();
    protected int maxMemorySize = 0;
    private ZombieState zombieState = ZombieState.NULL;

    protected ZombieAwareness(ZombieMainGoal mainGoal) {
        this.mainGoal = mainGoal;
    }

    public ZombieState getState() {
        return zombieState;
    }

    public void setState(ZombieState zombieState) {
        this.zombieState = zombieState;
    }

    public void tick() {
    }
}
