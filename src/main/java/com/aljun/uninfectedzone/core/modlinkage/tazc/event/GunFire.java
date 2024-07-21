package com.aljun.uninfectedzone.core.modlinkage.tazc.event;

import com.mojang.logging.LogUtils;
import com.tacz.guns.api.event.common.GunFireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

public class GunFire {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void onGunFire(GunFireEvent event) {
        LOGGER.info("GunFire!");
    }
}
