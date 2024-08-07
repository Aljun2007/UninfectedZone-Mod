package com.aljun.uninfectedzone.core.data.loot_table.conditions;

import com.aljun.uninfectedzone.core.modlinkage.tazc.LinkageGunMod;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LoadedGunAmmoAttachmentCondition implements LootItemCondition {

    private final ArrayList<String> ammo;
    private final ArrayList<String> guns;
    private final ArrayList<String> attachments;

    LoadedGunAmmoAttachmentCondition(List<String> guns, List<String> ammo, List<String> attachments) {
        this.guns = new ArrayList<>();
        this.guns.addAll(guns);
        this.ammo = new ArrayList<>();
        this.ammo.addAll(ammo);
        this.attachments = new ArrayList<>();
        this.attachments.addAll(attachments);
    }

    public static LootItemCondition.Builder gunAndAmmo(List<String> gun, List<String> ammo, List<String> attachments) {
        return () -> new LoadedGunAmmoAttachmentCondition(gun, ammo, attachments);
    }

    public @NotNull LootItemConditionType getType() {
        return UninfectedZoneLootItemConditions.LOADED_GUN_AMMO_ATTACHMENT;
    }

    public boolean test(LootContext p_81930_) {
        return LinkageGunMod.hasGun(this.guns) &&
                LinkageGunMod.hasAmmo(this.ammo) &&
                LinkageGunMod.hasAttachment(this.attachments);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LoadedGunAmmoAttachmentCondition> {
        public void serialize(@NotNull JsonObject jsonObject, @NotNull LoadedGunAmmoAttachmentCondition loadedGunAmmoAttachmentCondition, @NotNull JsonSerializationContext p_81945_) {

            JsonArray guns = new JsonArray();
            for (String gun : loadedGunAmmoAttachmentCondition.guns) {
                guns.add(gun);
            }
            jsonObject.add("guns", guns);

            JsonArray ammo = new JsonArray();
            for (String ammo1 : loadedGunAmmoAttachmentCondition.ammo) {
                ammo.add(ammo1);
            }
            jsonObject.add("ammo", ammo);

            JsonArray attachments = new JsonArray();
            for (String attachment : loadedGunAmmoAttachmentCondition.attachments) {
                attachments.add(attachment);
            }
            jsonObject.add("guns", attachments);

        }

        public @NotNull LoadedGunAmmoAttachmentCondition deserialize(@NotNull JsonObject jsonObject, @NotNull JsonDeserializationContext context) {

            List<String> gunsList = new ArrayList<>();
            List<String> ammoList = new ArrayList<>();
            List<String> attachmentsList = new ArrayList<>();

            if (jsonObject.has("guns")) {
                if (jsonObject.get("guns").isJsonArray()) {
                    JsonArray guns = jsonObject.getAsJsonArray("guns");
                    guns.forEach((jsonElement -> {
                        if (jsonElement.isJsonPrimitive()) {
                            gunsList.add(jsonElement.getAsString());
                        }
                    }));
                }
            }
            if (jsonObject.has("ammo")) {
                if (jsonObject.get("ammo").isJsonArray()) {
                    JsonArray ammo = jsonObject.getAsJsonArray("ammo");
                    ammo.forEach((jsonElement -> {
                        if (jsonElement.isJsonPrimitive()) {
                            ammoList.add(jsonElement.getAsString());
                        }
                    }));
                }
            }
            if (jsonObject.has("attachment")) {
                if (jsonObject.get("attachment").isJsonArray()) {
                    JsonArray attachments = jsonObject.getAsJsonArray("attachments");
                    attachments.forEach((jsonElement -> {
                        if (jsonElement.isJsonPrimitive()) {
                            attachmentsList.add(jsonElement.getAsString());
                        }
                    }));
                }
            }
            return new LoadedGunAmmoAttachmentCondition(gunsList, ammoList, attachmentsList);
        }
    }
}
