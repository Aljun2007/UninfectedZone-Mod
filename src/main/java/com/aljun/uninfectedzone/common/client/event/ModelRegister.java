package com.aljun.uninfectedzone.common.client.event;

import com.aljun.uninfectedzone.common.client.render.CustomZombieRender;
import com.aljun.uninfectedzone.common.minecraft.entity.UninfectedZoneEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelRegister {
    @SubscribeEvent
    public static void onClientSetUpEvent(FMLClientSetupEvent event) {
        EntityRenderers.register(UninfectedZoneEntityTypes.CUSTOM_ZOMBIE.get(), context -> new CustomZombieRender(context, false));
        EntityRenderers.register(UninfectedZoneEntityTypes.CUSTOM_ZOMBIE_SLIM.get(), context -> new CustomZombieRender(context, true));
    }
}
