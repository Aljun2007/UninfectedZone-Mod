package com.aljun.uninfectedzone.core.datapacks.conditions;

import com.google.gson.*;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LoadedModCondition implements LootItemCondition {
    final List<String> modIDs;

    LoadedModCondition(List<String> p_81923_) {
        this.modIDs = p_81923_;
    }

    public static LootItemCondition.Builder modID(String... modIDs) {
        return () -> new LoadedModCondition(new ArrayList<>(List.of(modIDs)));
    }

    public @NotNull LootItemConditionType getType() {
        return UninfectedZoneLootItemConditions.LOADED_MOD;
    }

    public boolean test(LootContext p_81930_) {
        for (String modID : modIDs) {
            if (!ModList.get().isLoaded(modID)) {
                return false;
            }
        }
        return true;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LoadedModCondition> {
        public void serialize(@NotNull JsonObject jsonObject, @NotNull LoadedModCondition loadedModCondition, @NotNull JsonSerializationContext p_81945_) {
            JsonArray array = new JsonArray();
            for (String modID : loadedModCondition.modIDs) {
                array.add(new JsonPrimitive(modID));
            }
            jsonObject.add("mod_ids", array);
        }

        public @NotNull LoadedModCondition deserialize(@NotNull JsonObject jsonObject, @NotNull JsonDeserializationContext context) {
            JsonArray array = jsonObject.getAsJsonArray("mod_ids");
            List<String> mod_ids = new ArrayList<>();
            array.forEach((jsonElement) -> {
                mod_ids.add(jsonElement.getAsString());
            });
            return new LoadedModCondition(mod_ids);
        }
    }
}
