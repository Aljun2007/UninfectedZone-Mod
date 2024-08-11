package com.aljun.uninfectedzone.core.modlinkage.tazc.event;

import com.aljun.uninfectedzone.common.zombie.abilities.Hearing;
import com.mojang.logging.LogUtils;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.api.event.server.AmmoHitBlockEvent;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import com.tacz.guns.config.common.GunConfig;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.data.attachment.Silence;
import com.tacz.guns.resource.pojo.data.gun.InaccuracyType;
import com.tacz.guns.util.AttachmentDataUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import java.util.Optional;

public class GunEventHandler {

    public static final Hearing.SoundType UN_SILENCER = Hearing.SoundType.EXPLOSION;
    public static final Hearing.SoundType SILENCER = Hearing.SoundType.custom(5, 0.5d);
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void onGunFire(GunFireEvent event) {
        if (event.getLogicalSide().isServer()) {
            ItemStack gunItem = event.getGunItemStack();
            LivingEntity shooter = event.getShooter();
            ResourceLocation gunId = ((AbstractGunItem) gunItem.getItem()).getGunId(event.getGunItemStack());
            Optional<CommonGunIndex> gunIndexOptional = TimelessAPI.getCommonGunIndex(gunId);
            if (gunIndexOptional.isPresent()) {
                CommonGunIndex gunIndex = gunIndexOptional.get();
                InaccuracyType inaccuracyState = InaccuracyType.getInaccuracyType(shooter);
                float[] inaccuracy = new float[]{gunIndex.getGunData().getInaccuracy(inaccuracyState)};
                int[] soundDistance = new int[]{GunConfig.DEFAULT_GUN_FIRE_SOUND_DISTANCE.get()};
                boolean[] useSilenceSound = new boolean[]{false};
                AttachmentDataUtils.getAllAttachmentData(gunItem, gunIndex.getGunData(), attachmentData -> {
                    if (!inaccuracyState.isAim()) {
                        inaccuracy[0] += attachmentData.getInaccuracyAddend();
                    }
                    Silence silence = attachmentData.getSilence();
                    if (silence != null) {
                        soundDistance[0] += silence.getDistanceAddend();
                        if (silence.isUseSilenceSound()) {
                            useSilenceSound[0] = true;
                        }
                    }
                });
                Hearing.Utils.sound(useSilenceSound[0] ? SILENCER : UN_SILENCER, (ServerLevel) event.getShooter().getLevel(), event.getShooter().blockPosition());
            }
        }
    }

    @SubscribeEvent
    public void onGunHit(AmmoHitBlockEvent event) {
        Hearing.Utils.sound(Hearing.SoundType.PROJECTILE, (ServerLevel) event.getLevel(), event.getHitResult().getBlockPos());
    }
}
