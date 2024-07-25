package com.aljun.uninfectedzone.core.modlinkage.tazc;

import com.aljun.uninfectedzone.core.modlinkage.tazc.event.GunFire;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

public class LinkageGunMod {
    public static final String MOD_ID = "tacz";
    private static boolean isLoaded = false;

    public static void init() {
        isLoaded = ModList.get().isLoaded(MOD_ID);
        try {
            if (isLoaded()) {
                MinecraftForge.EVENT_BUS.register(new GunFire());
            }
        } catch (NoClassDefFoundError ignore) {
        }
    }

    public static boolean isLoaded() {
        return isLoaded;
    }
}
