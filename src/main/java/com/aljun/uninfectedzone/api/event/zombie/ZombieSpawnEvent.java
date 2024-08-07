package com.aljun.uninfectedzone.api.event.zombie;

import net.minecraft.world.entity.Mob;

/**
 * 此事件在僵尸生成时加载护甲以及手持武器,
 * 并且中僵尸的 MainGoal <font color = "#ff0000">未加载
 *
 * @author Aljun2007
 */
public class ZombieSpawnEvent extends MobEvent {
    public ZombieSpawnEvent(Mob mob) {
        super(mob);
    }
}
