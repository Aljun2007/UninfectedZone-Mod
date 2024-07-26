package com.aljun.uninfectedzone.core.datapacks.conditions;

import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class UninfectedZoneLootItemConditions {
    public static final LootItemConditionType LOADED_GUN = new LootItemConditionType(new LoadedGunOrAmmoCondition.Serializer());
}
