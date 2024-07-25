package com.aljun.uninfectedzone.core.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;

public class ZombieHearingUtils {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void gunSound(LivingEntity shooter, int soundDistance, boolean useSilence) {
        LOGGER.info("shooter : {}\nsoundDistance :{}\nuseSilence : {}", shooter.position(), soundDistance, useSilence);
    }
}
