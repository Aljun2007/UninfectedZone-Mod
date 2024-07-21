package com.aljun.uninfectedzone.core.client.debug;

import com.aljun.uninfectedzone.core.client.debug.render.ChunkBorderRender;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientDebug {
    public static boolean isChunkBordenDisplay = false;
    public static ChunkBorderRender chunkBorderRender = new ChunkBorderRender(Minecraft.getInstance());
}
