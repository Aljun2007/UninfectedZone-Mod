package com.aljun.uninfectedzone.core.event.provider.zombie;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingEvent;

public class MobEvent extends LivingEvent {
    protected final Mob MOB;

    public MobEvent(Mob mob) {
        super(mob);
        this.MOB = mob;
    }

    public Mob getMob() {
        return MOB;
    }
}
