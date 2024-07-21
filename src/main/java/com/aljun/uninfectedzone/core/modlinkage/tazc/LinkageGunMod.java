package com.aljun.uninfectedzone.core.modlinkage.tazc;

import net.minecraftforge.fml.ModList;

public class LinkageGunMod {
    public static final String MOD_ID = "tacz";
    private static boolean isLoaded = false;

    public static void init() {
        isLoaded = ModList.get().isLoaded(MOD_ID);
    }

    public static boolean isLoaded() {
        return isLoaded;
    }
}
