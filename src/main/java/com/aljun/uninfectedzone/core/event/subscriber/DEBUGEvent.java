package com.aljun.uninfectedzone.core.event.subscriber;


import com.aljun.uninfectedzone.core.debug.Debug;
import com.aljun.uninfectedzone.core.utils.TagUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.Objects;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DEBUGEvent {
    public static Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (!event.getPlayer().getLevel().isClientSide()) {
            ServerPlayer player = (ServerPlayer) event.getPlayer();

            ItemStack stack = player.getMainHandItem();
            if (TagUtils.fastRead(TagUtils.getRootOrNull(stack), Debug.SNATCHER_ITEM)) {
                if (event.getTarget() instanceof LivingEntity livingEntity) {
                    player.addItem(livingEntity.getItemBySlot(EquipmentSlot.MAINHAND).copy());
                    player.addItem(livingEntity.getItemBySlot(EquipmentSlot.OFFHAND).copy());
                    player.addItem(livingEntity.getItemBySlot(EquipmentSlot.HEAD).copy());
                    player.addItem(livingEntity.getItemBySlot(EquipmentSlot.CHEST).copy());
                    player.addItem(livingEntity.getItemBySlot(EquipmentSlot.LEGS).copy());
                    player.addItem(livingEntity.getItemBySlot(EquipmentSlot.FEET).copy());
                }
            }
            if (TagUtils.fastRead(TagUtils.getRootOrNull(stack), Debug.KILLER_ITEM)) {
                event.getTarget().kill();
            }
        }
    }

    @SubscribeEvent
    public static void onUse(EntityItemPickupEvent event) {
        if (!event.getEntity().getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                ItemStack stack = event.getItem().getItem();
                if (TagUtils.fastRead(TagUtils.getRootOrNull(stack), Debug.HEAL_ITEM)) {
                    player.removeAllEffects();
                    player.clearFire();
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 10, 8));
                    player.addEffect(new MobEffectInstance(MobEffects.HEAL, 10, 8));
                }
                if (TagUtils.fastRead(TagUtils.getRootOrNull(stack), Debug.DAY_ITEM)) {
                    try {
                        for (ServerLevel serverlevel : Objects.requireNonNull(event.getEntity().getServer()).getAllLevels()) {
                            serverlevel.setDayTime(1000L);
                        }
                    } catch (NullPointerException ignore) {
                    }
                }
                if (TagUtils.fastRead(TagUtils.getRootOrNull(stack), Debug.NIGHT_ITEM)) {
                    try {
                        for (ServerLevel serverlevel : Objects.requireNonNull(event.getEntity().getServer()).getAllLevels()) {
                            serverlevel.setDayTime(13000L);
                        }
                    } catch (NullPointerException ignore) {
                    }
                }
                if (TagUtils.fastRead(TagUtils.getRootOrNull(stack), Debug.TEST)) {
                    Debug.test(event.getEntityLiving().getLevel());
                }
            }
        }
    }
}
