package com.aljun.uninfectedzone.core.modlinkage.cgm.event;

import com.aljun.uninfectedzone.common.zombie.abilities.Hearing;
import com.mojang.logging.LogUtils;
import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.event.GunProjectileHitEvent;
import com.mrcrayfish.guns.util.GunModifierHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

public class GunEventHandler {

    public static final Hearing.SoundType UN_SILENCER = Hearing.SoundType.EXPLOSION;
    public static final Hearing.SoundType SILENCER = Hearing.SoundType.custom(5, 0.5d);
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void onGunFire(GunFireEvent.Post event) {
        if (!event.isClient()) {
            boolean isSilencer = GunModifierHelper.isSilencedFire(event.getStack());
            Hearing.Utils.sound(isSilencer ? SILENCER : UN_SILENCER, (ServerLevel) event.getPlayer().getLevel(), event.getPlayer().blockPosition());
        }
    }

    @SubscribeEvent
    public void onGunHit(GunProjectileHitEvent event) {
        if (!event.getProjectile().getLevel().isClientSide()) {
            Hearing.Utils.sound(Hearing.SoundType.PROJECTILE, (ServerLevel) event.getProjectile().getLevel(), new BlockPos(event.getRayTrace().getLocation()));
        }
    }
}
