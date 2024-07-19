package com.aljun.uninfectedzone.core.zombie.awareness;

public class ZombieState {
    public static final ZombieState NULL = new ZombieState("null");

    private static int quantity = -1;
    public final int ID;
    public final String KEY;

    public ZombieState(String key) {
        this.ID = ++quantity;
        this.KEY = key;
    }

    public boolean is(ZombieState zombieState) {
        return zombieState.ID == this.ID;
    }
}