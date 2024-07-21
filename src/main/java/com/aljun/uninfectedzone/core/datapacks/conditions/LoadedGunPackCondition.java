package com.aljun.uninfectedzone.core.datapacks.conditions;

import com.google.gson.*;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LoadedGunPackCondition implements LootItemCondition {
    final List<String> gunPackIDs;

    LoadedGunPackCondition(List<String> p_81923_) {
        this.gunPackIDs = p_81923_;
    }

    public static Builder modID(String... modIDs) {
        return () -> new LoadedGunPackCondition(new ArrayList<>(List.of(modIDs)));
    }

    public @NotNull LootItemConditionType getType() {
        return UninfectedZoneLootItemConditions.LOADED_MOD;
    }

    public boolean test(LootContext p_81930_) {
        for (String id : gunPackIDs) {
            if (!isLoaded(id)) {
                return false;
            }
        }
        return true;
    }

    private boolean isLoaded(String id) {
        //ResourceManager.EXTRA_ENTRIES.
        return false;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LoadedGunPackCondition> {
        public void serialize(@NotNull JsonObject jsonObject, @NotNull LoadedGunPackCondition loadedModCondition, @NotNull JsonSerializationContext p_81945_) {
            JsonArray array = new JsonArray();
            for (String ids : loadedModCondition.gunPackIDs) {
                array.add(new JsonPrimitive(ids));
            }
            jsonObject.add("mod_ids", array);
        }

        public @NotNull LoadedGunPackCondition deserialize(@NotNull JsonObject jsonObject, @NotNull JsonDeserializationContext context) {
            JsonArray array = jsonObject.getAsJsonArray("gun_pack_ids");
            List<String> ids = new ArrayList<>();
            array.forEach((jsonElement) -> {
                ids.add(jsonElement.getAsString());
            });
            return new LoadedGunPackCondition(ids);
        }
    }
}
