package com.aljun.uninfectedzone.common.event.register;


import com.aljun.uninfectedzone.common.zombie.abilities.Hearing;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WorldHandler {
    @SubscribeEvent
    public static void onExploring(ExplosionEvent event) {
        if (!event.getWorld().isClientSide()) {
            Hearing.Utils.sound(Hearing.SoundType.EXPLOSION, (ServerLevel) event.getWorld(), new BlockPos(event.getExplosion().getPosition()));
        }
    }

    @SubscribeEvent
    public static void onBlockDestroyed(BlockEvent.BreakEvent event) {
        if (!event.getWorld().isClientSide()) {
            Hearing.Utils.sound(Hearing.SoundType.BLOCK, (ServerLevel) event.getWorld(), event.getPos());
        }
    }

    @SubscribeEvent
    public static void onBlockDestroyed(BlockEvent.EntityPlaceEvent event) {
        if (!event.getWorld().isClientSide()) {
            Hearing.Utils.sound(Hearing.SoundType.BLOCK, (ServerLevel) event.getWorld(), event.getPos());
        }
    }

}