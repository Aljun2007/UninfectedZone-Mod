package com.aljun.uninfectedzone.api.register;

import com.aljun.uninfectedzone.core.registry.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import com.aljun.uninfectedzone.core.zombie.speedtypes.ZombieSpeedType;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class UninfectedZoneRegisterAPI {
    public static class Registries {
        public final static Supplier<Supplier<IForgeRegistry<ZombieLike>>> ZOMBIE_LIKES
                = () -> UninfectedZoneRegistry.ZOMBIE_LIKES;
        public final static Supplier<Supplier<IForgeRegistry<ZombieSpeedType>>> ZOMBIE_SPEED_TYPES
                = () -> UninfectedZoneRegistry.ZOMBIE_SPEED_TYPES;
        public final static Supplier<Supplier<IForgeRegistry<ZombieAbility>>> ZOMBIE_ABILITIES
                = () -> UninfectedZoneRegistry.ZOMBIE_ABILITIES;
    }
}
