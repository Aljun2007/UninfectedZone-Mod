package com.aljun.uninfectedzone.core.event.provider.zombie;

import net.minecraft.world.entity.Mob;


/**
 * 这个事件在僵尸生成时加载护甲以及手持武器
 * */
public class ZombieSpawnEvent extends MobEvent {
    public ZombieSpawnEvent(Mob mob) {
        super(mob);
    }
}
