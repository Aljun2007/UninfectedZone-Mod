package com.aljun.uninfectedzone.common.zombie.attribute;

import com.aljun.uninfectedzone.UninfectedZone;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ZombieAttributes {

    public static Supplier<Attribute> SUN_SENSITIVE;
    public static Supplier<Attribute> CONVERT_IN_WATER;

    public static Supplier<Attribute> DIG_SPEED;
    public static Supplier<Attribute> SMELLING_DISTANCE;
    public static Supplier<Attribute> HEARING_DISTANCE;

    public static void register(IForgeRegistry<Attribute> registry) {
        DIG_SPEED = register(registry, () -> new RangedAttribute(UninfectedZone.MOD_ID + ".digSpeed", 1d, 0d, 64d).setSyncable(true), "dig_speed");
        SMELLING_DISTANCE = register(registry, () -> new RangedAttribute(UninfectedZone.MOD_ID + ".smellingDistance", 25d, 0d, 256d).setSyncable(true), "smelling_distance");
        HEARING_DISTANCE = register(registry, () -> new RangedAttribute(UninfectedZone.MOD_ID + ".hearingDistance", 200d, 0d, 2048d).setSyncable(true), "hearing_distance");
        SUN_SENSITIVE = register(registry, () -> new RangedAttribute(UninfectedZone.MOD_ID + ".sunSensitive", 1d, 0d, 1d).setSyncable(true), "sun_sensitive");
        CONVERT_IN_WATER = register(registry, () -> new RangedAttribute(UninfectedZone.MOD_ID + ".convertInWater", 1d, 0d, 1d).setSyncable(true), "convert_in_water");
    }

    private static <T extends Attribute> Supplier<T> register(IForgeRegistry<Attribute> registry, Supplier<T> instance, String key) {
        T attribute = instance.get();
        attribute.setRegistryName(key);
        registry.register(attribute);
        return () -> attribute;
    }

    public static void buildSupplier(AttributeSupplier.Builder returnValue) {
        returnValue.add(SUN_SENSITIVE.get())
                .add(CONVERT_IN_WATER.get())
                .add(DIG_SPEED.get())
                .add(SMELLING_DISTANCE.get())
                .add(HEARING_DISTANCE.get());

    }
}
