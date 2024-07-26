package com.aljun.uninfectedzone;

import com.aljun.uninfectedzone.core.config.GlobalConfigs;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.file.config.ConfigFileLoader;
import com.aljun.uninfectedzone.core.forgeRegister.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.core.modlinkage.tazc.LinkageGunMod;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.io.File;
import java.util.Objects;

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
        LOGGER.info("UninfectedZoneDirectory:{}", gamePath);
        LinkageGunMod.init();
    }

    public static String getAbsPath() {
        return gamePath;
    }

    public static void afterRegister() {
        UninfectedZoneRegistry.ZOMBIE_LIKES.get().forEach((like) -> {
            try {
                LOGGER.info(Objects.requireNonNull(like.getRegistryName()).toString());
            } catch (NullPointerException ignored) {
            }
        });
        UninfectedZoneConfig.stopRegister();
        UninfectedZone.loadAllConfigJsons();
        LOGGER.info("TestBoolean :{}", UninfectedZoneConfig.get(GlobalConfigs.TEST_BOOLEAN.get()));
    }

    private static void loadAllConfigJsons() {
        ConfigFileLoader.loadGlobalConfigJson();
    }
}
