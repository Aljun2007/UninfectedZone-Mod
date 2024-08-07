package com.aljun.uninfectedzone.core.data.loot_table.conditions;

import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ZombieLikeCondition implements LootItemCondition {
    private final ArrayList<String> zombieLikes;

    private ZombieLikeCondition(List<String> zombieLikes) {
        this.zombieLikes = new ArrayList<>();
        this.zombieLikes.addAll(zombieLikes);
    }

    public static LootItemCondition.Builder zombieLike(List<String> zombieLikes) {
        return () -> new ZombieLikeCondition(zombieLikes);
    }

    public @NotNull LootItemConditionType getType() {
        return UninfectedZoneLootItemConditions.ZOMBIE_LIKE;
    }

    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(LootContext.EntityTarget.THIS.getParam());
        if (entity != null) {
            if (entity instanceof Mob mob) {
                String id = ZombieUtils.getZombieLikeID(mob);
                return this.zombieLikes.contains(id);
            }
        }
        return false;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ZombieLikeCondition> {
        public void serialize(@NotNull JsonObject jsonObject, @NotNull ZombieLikeCondition zombieLikeCondition, @NotNull JsonSerializationContext context) {
            JsonArray zombieLikes = new JsonArray();
            for (String zombieLike : zombieLikeCondition.zombieLikes) {
                zombieLikes.add(zombieLike);
            }
            jsonObject.add("zombie_likes", zombieLikes);
        }

        public @NotNull ZombieLikeCondition deserialize(@NotNull JsonObject jsonObject, @NotNull JsonDeserializationContext context) {
            List<String> zombieLikeList = new ArrayList<>();
            if (jsonObject.has("zombie_likes")) {
                if (jsonObject.get("zombie_likes").isJsonArray()) {
                    JsonArray zombieLikes = jsonObject.getAsJsonArray("zombie_likes");
                    zombieLikes.forEach((jsonElement -> {
                        if (jsonElement.isJsonPrimitive()) {
                            zombieLikeList.add(jsonElement.getAsString());
                        }
                    }));
                }
            }
            return new ZombieLikeCondition(zombieLikeList);
        }
    }
}
