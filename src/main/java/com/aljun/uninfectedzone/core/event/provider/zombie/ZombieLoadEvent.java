package com.aljun.uninfectedzone.core.event.provider.zombie;

import net.minecraft.world.entity.Mob;

/**
 * 此事件中僵尸的 MainGoal <font color = "#ff0000">未加载
 */

public class ZombieLoadEvent extends MobEvent {
    public ZombieLoadEvent(Mob mob) {
        super(mob);
    }
}
