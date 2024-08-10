package com.aljun.uninfectedzone.core.data.loot_table.conditions;

import com.aljun.uninfectedzone.UninfectedZone;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class UninfectedZoneLootItemConditions {
    public static final LootItemConditionType LOADED_GUN_AMMO_ATTACHMENT =
            new LootItemConditionType(new LoadedGunAmmoAttachmentCondition.Serializer());
    public static final LootItemConditionType ZOMBIE_LIKE =
            new LootItemConditionType(new ZombieLikeCondition.Serializer());
    public static final LootItemConditionType ENTITY_TYPE =
            new LootItemConditionType(new EntityTypeCondition.Serializer());

    public static void register() {
        Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(UninfectedZone.MOD_ID, "loaded_guns_ammo_attachments"), LOADED_GUN_AMMO_ATTACHMENT);
        Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(UninfectedZone.MOD_ID, "zombie_like"), ZOMBIE_LIKE);
        Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(UninfectedZone.MOD_ID, "entity_type"), ENTITY_TYPE);
    }
}
