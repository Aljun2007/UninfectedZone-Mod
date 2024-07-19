package com.aljun.uninfectedzone.core.event.subscriber;


import com.aljun.uninfectedzone.core.forgeRegister.UninfectedZoneRegistry;
import com.mojang.logging.LogUtils;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DEBUG {
    public static Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {

        UninfectedZoneRegistry.ZOMBIE_LIKES.get().getEntries();
    }
}
