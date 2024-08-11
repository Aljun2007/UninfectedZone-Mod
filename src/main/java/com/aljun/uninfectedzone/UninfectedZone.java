package com.aljun.uninfectedzone;

import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.modlinkage.cgm.LinkageCGM;
import com.aljun.uninfectedzone.core.modlinkage.tazc.LinkageTACZ;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.io.File;

@Mod(UninfectedZone.MOD_ID)
public class UninfectedZone {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "uninfectedzone";
    public static final String MOD_DISPLAY_NAME = "Uninfected Zone Mod";
    public static final String MOD_CAMEL_NAME = "UninfectedZoneMod";
    public static boolean debugMode = true;
    public static boolean useZombieResourcePack = true;
    public static boolean useZombiePackSounds = false;
    private static String gamePath = "C:\\Minecraft\\" + MOD_ID;

    public UninfectedZone() {
        try {
            Minecraft minecraft = Minecraft.getInstance();
            File file = new File(minecraft.gameDirectory.getAbsolutePath());
            gamePath = file.getParent() + "\\" + MOD_ID;
        } catch (NullPointerException ignore) {
        }
        File file = new File(gamePath);
        file.mkdirs();
        LinkageTACZ.init();
        LinkageCGM.init();
    }

    public static String getAbsPath() {
        return gamePath;
    }

    public static void afterRegister() {
        UninfectedZoneConfig.stopRegister();
        UninfectedZoneConfig.init();
    }


}
