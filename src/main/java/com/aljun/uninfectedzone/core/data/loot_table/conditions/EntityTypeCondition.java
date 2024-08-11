package com.aljun.uninfectedzone.core.data.loot_table.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityTypeCondition implements LootItemCondition {
    private final ArrayList<ResourceLocation> entityTypes;

    private EntityTypeCondition(List<ResourceLocation> entityTypes) {
        this.entityTypes = new ArrayList<>();
        this.entityTypes.addAll(entityTypes);
    }

    public static LootItemCondition.Builder entityType(List<EntityType<?>> entityTypes) {
        return () -> new EntityTypeCondition(toList(entityTypes));
    }

    private static List<ResourceLocation> toList(List<EntityType<?>> entityTypes) {
        List<ResourceLocation> resourceLocations = new ArrayList<>();
        entityTypes.forEach(entityType -> {
            ResourceLocation resourceLocation = entityType.getRegistryName();
            if (resourceLocation != null) {
                resourceLocations.add(resourceLocation);
            }
        });
        return resourceLocations;
    }

    public static LootItemCondition.Builder entityType(EntityType<?>... entityTypes) {

        return () -> new EntityTypeCondition(toList(List.of(entityTypes)));
    }

    public static LootItemCondition.Builder entityType(String... entityTypes) {
        List<ResourceLocation> resourceLocations = new ArrayList<>();
        Arrays.stream(entityTypes).forEach(string -> {
            resourceLocations.add(new ResourceLocation(string));
        });
        return () -> new EntityTypeCondition(resourceLocations);
    }

    public @NotNull LootItemConditionType getType() {
        return UninfectedZoneLootItemConditions.ZOMBIE_LIKE;
    }

    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(LootContext.EntityTarget.THIS.getParam());
        if (entity != null) {
            ResourceLocation entityType = entity.getType().getRegistryName();
            if (entityType != null) {
                return this.entityTypes.contains(entityType);
            }
        }
        return false;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<EntityTypeCondition> {
        public void serialize(@NotNull JsonObject jsonObject, @NotNull EntityTypeCondition entityTypeCondition, @NotNull JsonSerializationContext context) {

            JsonArray entityTypes = new JsonArray();
            for (ResourceLocation entityType : entityTypeCondition.entityTypes) {
                entityTypes.add(entityType.toString());
            }
            jsonObject.add("entity_types", entityTypes);

        }

        public @NotNull EntityTypeCondition deserialize(@NotNull JsonObject jsonObject, @NotNull JsonDeserializationContext context) {
            List<ResourceLocation> entityTypeList = new ArrayList<>();
            if (jsonObject.has("entity_types")) {
                if (jsonObject.get("entity_types").isJsonArray()) {
                    JsonArray entityTypes = jsonObject.getAsJsonArray("entity_types");
                    entityTypes.forEach(jsonElement -> {
                        if (jsonElement.isJsonPrimitive()) {
                            entityTypeList.add(new ResourceLocation(jsonElement.getAsString()));
                        }
                    });
                }
            }
            return new EntityTypeCondition(entityTypeList);
        }
    }
}
