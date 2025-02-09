package com.aljun.uninfectedzone.core.modlinkage.cgm;

import com.aljun.uninfectedzone.core.modlinkage.cgm.event.GunEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

public class LinkageCGM {
    public static final String MOD_ID = "cgm";
    private static boolean isLoaded = false;

    public static void init() {
        isLoaded = ModList.get().isLoaded(MOD_ID);
        try {
            if (isLoaded()) {
                MinecraftForge.EVENT_BUS.register(new GunEventHandler());
            }
        } catch (NoClassDefFoundError ignore) {
        }
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

}
