package com.aljun.uninfectedzone.api.event.zombie;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Event;

public class LivingInfectionConvertEvent extends Event {

    private final LivingEntity INCOME;
    private LivingEntity outcome;

    public LivingInfectionConvertEvent(LivingEntity income, Mob outcome) {
        this.INCOME = income;
        this.outcome = outcome;
    }

    public LivingEntity getOutcome() {
        return this.outcome;
    }

    public void setOutcome(LivingEntity outcome) {
        this.outcome = outcome;
    }

    public LivingEntity getIncome() {
        return this.INCOME;
    }
}
