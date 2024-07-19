package com.aljun.uninfectedzone.core.event.provider.zombie;

import net.minecraft.world.entity.Mob;

public class ZombieLoadEvent extends MobEvent {
    public ZombieLoadEvent(Mob mob) {
        super(mob);
    }
}
