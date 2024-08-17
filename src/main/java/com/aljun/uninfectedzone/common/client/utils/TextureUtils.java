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

    public static ResourceLocation slimCustomZombieFromSeed(int uuid) {

        List<String> list = UninfectedZoneConfig.get(ConfigSets.Client.CUSTOM_ZOMBIE_SLIM_TEXTURE_PATH.get());
        return new ResourceLocation(list.get(uuid % list.size()));

    }

    public static ResourceLocation defaultCustomZombieFromSeed(int uuid) {
        if (usePack()) {
            return randomZombiePackSkin(uuid);
        } else {
            List<String> list = UninfectedZoneConfig.get(ConfigSets.Client.CUSTOM_ZOMBIE_TEXTURE_PATH.get());
            return new ResourceLocation(list.get(uuid % list.size()));
        }

    }

    public static boolean usePack() {
        return UninfectedZone.useZombieResourcePack;
    }

    public static ResourceLocation randomZombiePackSkin(int uuid) {
        return new ResourceLocation("minecraft:optifine/random/entity/zombie/zombie" + (1 + uuid % 1107) + ".png");
    }

    public static ResourceLocation randomHuskPackSkin(int uuid) {
        return new ResourceLocation("minecraft:optifine/random/entity/zombie/husk" + (1 + uuid % 100) + ".png");
    }

    public static ResourceLocation randomDrownedPackSkin(int uuid) {
        return new ResourceLocation("minecraft:optifine/random/entity/zombie/drowned" + (1 + uuid % 50) + ".png");
    }

    public static ResourceLocation randomHuskEye(int uuid) {
        return new ResourceLocation("minecraft:optifine/random/entity/zombie/husk" + (1 + uuid % 100) + "_e.png");
    }

    public static ResourceLocation randomZombieEye(int uuid) {
        return new ResourceLocation("minecraft:optifine/random/entity/zombie/zombie" + (1 + uuid % 1107) + "_e.png");
    }

    public static boolean useEye() {
        return true;
    }
}
