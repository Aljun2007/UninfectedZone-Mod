package com.aljun.uninfectedzone.api.event.zombie;

import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.eventbus.api.Event;

public class SetZombieSunSensitiveEvent extends Event {
    private final Zombie ZOMBIE;
    private final boolean defaultSunSensitive;
    private boolean isDirty = false;
    private boolean isSunSensitive;

    public SetZombieSunSensitiveEvent(Zombie zombie, boolean defaultSunSensitive) {
        this.ZOMBIE = zombie;
        this.isSunSensitive = defaultSunSensitive;
        this.defaultSunSensitive = defaultSunSensitive;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public Zombie getZombie() {
        return ZOMBIE;
    }

    public boolean isSunSensitive() {
        return this.isDirty ? this.isSunSensitive : this.defaultSunSensitive;
    }

    public void setSunSensitive(boolean isSunSensitive) {
        this.isDirty = true;
        this.isSunSensitive = isSunSensitive;
    }

    public void setDefault() {
        this.isDirty = false;
        this.isSunSensitive = this.defaultSunSensitive;
    }

    public boolean isDirty() {
        return this.isDirty;
    }
}
