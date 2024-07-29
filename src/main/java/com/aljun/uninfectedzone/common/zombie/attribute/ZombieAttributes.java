package com.aljun.uninfectedzone.common.zombie.attribute;

import com.aljun.uninfectedzone.UninfectedZone;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ZombieAttributes {

    public static Supplier<Attribute> DIG_SPEED;
    public static Supplier<Attribute> SMELLING_DISTANCE;
    public static Supplier<Attribute> HEARING_DISTANCE;

    public static void register(IForgeRegistry<Attribute> registry) {
        DIG_SPEED = register(registry, () -> new RangedAttribute(UninfectedZone.MOD_ID + ".digSpeed", 1d, 0d, 256d).setSyncable(true), "dig_speed");
        SMELLING_DISTANCE = register(registry, () -> new RangedAttribute(UninfectedZone.MOD_ID + ".smellingDistance", 25d, 0d, 256d).setSyncable(true), "smelling_distance");
        HEARING_DISTANCE = register(registry, () -> new RangedAttribute(UninfectedZone.MOD_ID + ".hearingDistance", 200d, 0d, 2048d).setSyncable(true), "hearing_distance");
    }

    private static <T extends Attribute> Supplier<T> register(IForgeRegistry<Attribute> registry, Supplier<T> instance, String key) {
        T attribute = instance.get();
        attribute.setRegistryName(key);
        registry.register(attribute);
        return () -> attribute;
    }
}
