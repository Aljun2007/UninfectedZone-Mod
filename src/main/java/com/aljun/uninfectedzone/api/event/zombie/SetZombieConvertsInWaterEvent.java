package com.aljun.uninfectedzone.api.event.zombie;

import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.eventbus.api.Event;

public class SetZombieConvertsInWaterEvent extends Event {
    private final Zombie ZOMBIE;
    private final boolean defaultConvertsInWater;
    private boolean isDirty = false;
    private boolean convertsInWater;

    public SetZombieConvertsInWaterEvent(Zombie zombie, boolean defaultConvertsInWater) {
        this.ZOMBIE = zombie;
        this.convertsInWater = defaultConvertsInWater;
        this.defaultConvertsInWater = defaultConvertsInWater;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public Zombie getZombie() {
        return ZOMBIE;
    }

    public boolean convertsInWater() {
        return this.isDirty ? this.convertsInWater : this.defaultConvertsInWater;
    }

    public void setConvertsInWater(boolean convertsInWater) {
        this.isDirty = true;
        this.convertsInWater = convertsInWater;
    }

    public void setDefault() {
        this.isDirty = false;
        this.convertsInWater = this.defaultConvertsInWater;
    }

    public boolean isDirty() {
        return this.isDirty;
    }
}
