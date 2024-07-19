package com.aljun.uninfectedzone.deafult.entity;

import com.aljun.uninfectedzone.core.entity.InfectionConvertType;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class InfectionConvertTypes {

    private static final InfectionConvertType VILLAGER_TO_ZOMBIE_VILLAGER =
            new InfectionConvertType(() -> EntityType.VILLAGER, () -> EntityType.ZOMBIE_VILLAGER);

    public static void register(IForgeRegistry<InfectionConvertType> registry) {
        register(registry, () -> VILLAGER_TO_ZOMBIE_VILLAGER, "villager_to_zombie_villager");
    }

    private static void register(IForgeRegistry<InfectionConvertType> registry, Supplier<InfectionConvertType> instance, String key) {
        InfectionConvertType type = instance.get().setRegistryName(key);
        registry.register(type);
    }

}
