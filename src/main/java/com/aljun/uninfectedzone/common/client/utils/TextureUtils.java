package com.aljun.uninfectedzone.common.client.utils;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.common.config.ConfigSets;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TextureUtils {

    public static ResourceLocation slimCustomZombieFromSeed(int id) {

        List<String> list = UninfectedZoneConfig.get(ConfigSets.Client.CUSTOM_ZOMBIE_SLIM_TEXTURE_PATH.get());
        return new ResourceLocation(list.get(id % list.size()));

    }

    public static ResourceLocation defaultCustomZombieFromSeed(int id) {

        if (usePack()) {
            return new ResourceLocation("minecraft:optifine/random/entity/zombie/zombie" + id % 1108 + ".png");
        } else {
            List<String> list = UninfectedZoneConfig.get(ConfigSets.Client.CUSTOM_ZOMBIE_TEXTURE_PATH.get());
            return new ResourceLocation(list.get(id % list.size()));
        }

    }

    public static boolean usePack() {
        return UninfectedZone.useZombieResourcePack;
    }
}
