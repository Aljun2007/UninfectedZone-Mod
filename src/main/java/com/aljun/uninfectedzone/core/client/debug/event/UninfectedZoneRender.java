package com.aljun.uninfectedzone.core.client.debug.event;

import com.aljun.uninfectedzone.core.client.debug.ClientDebug;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class UninfectedZoneRender {
    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            if (ClientDebug.isChunkBordenDisplay) {
                Vec3 vec3 = event.getCamera().getPosition();
                ClientDebug.chunkBorderRender.render(vec3.x, vec3.y, vec3.z);
            }
        }

    }

}
