package com.aljun.uninfectedzone.common.config.data.pool;

import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.common.zombie.zombieLikes.ZombieLikes;
import com.aljun.uninfectedzone.core.data.pool.ZombieLikePool;

import java.util.function.Supplier;

public class ZombieLikePools {
    public static Supplier<ZombieLikePool> testPool =
            () -> new ZombieLikePool()
                    .add(ZombieLikes.TEST_ZOMBIE.get(), 1d)
                    .add(ZombieLike.DUMMY.get(), 1d);
}
