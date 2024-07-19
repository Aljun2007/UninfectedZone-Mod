package com.aljun.uninfectedzone.core.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class InfectionConvertType extends ForgeRegistryEntry<InfectionConvertType> {
    public final Supplier<EntityType<?>> INCOME;
    public final Supplier<EntityType<?>> OUTCOME;


    public InfectionConvertType(Supplier<EntityType<?>> income, Supplier<EntityType<?>> outcome) {
        this.INCOME = income;
        this.OUTCOME = outcome;
    }

}
