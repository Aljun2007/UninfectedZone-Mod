package com.aljun.uninfectedzone.core.client.event;

import com.aljun.uninfectedzone.core.utils.LogicalUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class OnClientSetUp {
    public static void onClientSetUp(FMLClientSetupEvent event) {
        LogicalUtils.isClient = true;
    }
}
