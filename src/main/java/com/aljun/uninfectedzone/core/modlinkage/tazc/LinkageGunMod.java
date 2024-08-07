package com.aljun.uninfectedzone.core.modlinkage.tazc;

import com.aljun.uninfectedzone.core.modlinkage.tazc.event.GunFire;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.resource.index.CommonAmmoIndex;
import com.tacz.guns.resource.index.CommonAttachmentIndex;
import com.tacz.guns.resource.index.CommonGunIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.Map;

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

    public static boolean hasGun(ArrayList<String> guns) {
        if (guns.isEmpty()) return true;
        try {
            if (isLoaded()) {
                for (String gun : guns) {
                    if (!hasGun(gun)) return false;
                }
            }
        } catch (NoClassDefFoundError ignore) {
            return false;
        }
        return true;
    }

    public static boolean hasGun(String gun) {
        try {
            if (isLoaded()) {
                for (Map.Entry<ResourceLocation, CommonGunIndex> entry : TimelessAPI.getAllCommonGunIndex()) {
                    if (entry.getKey().toString().equals(gun)) {
                        return true;
                    }
                }
            }
        } catch (NoClassDefFoundError ignore) {
        }
        return false;
    }

    public static boolean hasAmmo(ArrayList<String> ammo) {
        if (ammo.isEmpty()) return true;
        try {
            if (isLoaded()) {
                for (String ammo1 : ammo) {
                    if (!hasAmmo(ammo1)) return false;
                }
            }
        } catch (NoClassDefFoundError ignore) {
            return false;
        }
        return true;
    }

    public static boolean hasAmmo(String ammo) {
        try {
            if (isLoaded()) {
                for (Map.Entry<ResourceLocation, CommonAmmoIndex> entry : TimelessAPI.getAllCommonAmmoIndex()) {
                    if (entry.getKey().toString().equals(ammo)) {
                        return true;
                    }
                }
            }
        } catch (NoClassDefFoundError ignore) {
        }
        return false;
    }

    public static boolean hasAttachment(ArrayList<String> attachments) {
        if (attachments.isEmpty()) return true;
        try {
            if (isLoaded()) {
                for (String attachment : attachments)
                    if (!hasAttachment(attachment)) return false;
            }
        } catch (NoClassDefFoundError ignore) {
            return false;
        }
        return true;
    }

    public static boolean hasAttachment(String attachment) {
        try {
            if (isLoaded()) {
                for (Map.Entry<ResourceLocation, CommonAttachmentIndex> entry : TimelessAPI.getAllCommonAttachmentIndex()) {
                    if (entry.getKey().toString().equals(attachment)) {
                        return true;
                    }
                }
            }
        } catch (NoClassDefFoundError ignore) {
        }
        return false;
    }
}
