package com.aljun.uninfectedzone;

import com.aljun.uninfectedzone.core.modlinkage.tazc.LinkageGunMod;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.io.File;

@Mod(UninfectedZone.MOD_ID)
public class UninfectedZone {
    public static final String MOD_ID = "uninfectedzone";
    public static final String MOD_DISPLAY_NAME = "Uninfected Zone Mod";
    public static final String MOD_CAMEL_NAME = "UninfectedZoneMod";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static boolean debugMode = true;
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
        LOGGER.info(gamePath);
        LinkageGunMod.init();
        //CommonGunPackLoader
    }

    public static void main(String[] args) {
        File file = new File(gamePath);
        file.mkdirs();
        System.out.println(getAbsPath());
        file.listFiles();
    }

    public static String getAbsPath() {
        return gamePath;
    }
}
