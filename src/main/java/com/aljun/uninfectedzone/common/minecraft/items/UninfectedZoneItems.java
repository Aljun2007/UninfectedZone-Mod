package com.aljun.uninfectedzone.common.minecraft.items;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.common.minecraft.entity.UninfectedZoneEntityTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class UninfectedZoneItems {

    public static Supplier<Item> CUSTOM_ZOMBIE_SLIM_SPAWN_EGG;
    public static Supplier<Item> CUSTOM_ZOMBIE_SPAWN_EGG;

    public static void register(IForgeRegistry<Item> registry) {
        CUSTOM_ZOMBIE_SPAWN_EGG = register(registry, () -> new ForgeSpawnEggItem(() -> UninfectedZoneEntityTypes.CUSTOM_ZOMBIE.get(), 44975, 3419431, ((new Item.Properties()).tab(CreativeModeTab.TAB_MISC))), "custom_zombie_spawn_egg");
        CUSTOM_ZOMBIE_SLIM_SPAWN_EGG = register(registry, () -> new ForgeSpawnEggItem(() -> UninfectedZoneEntityTypes.CUSTOM_ZOMBIE_SLIM.get(), 44975, 803406, ((new Item.Properties()).tab(CreativeModeTab.TAB_MISC))), "custom_zombie_slim_spawn_egg");
    }

    private static <T extends Item> Supplier<T> register(IForgeRegistry<Item> registry, Supplier<T> instance, String key) {
        T item = instance.get();
        item.setRegistryName(UninfectedZone.MOD_ID, key);
        registry.register(item);
        return () -> item;
    }

}
