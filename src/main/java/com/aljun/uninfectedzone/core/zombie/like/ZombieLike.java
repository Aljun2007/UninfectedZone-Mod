package com.aljun.uninfectedzone.core.zombie.like;

import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.ZombieLoadUtils;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashMap;

public abstract class ZombieLike extends ForgeRegistryEntry<ZombieLike> {
    private static final HashMap<ResourceLocation, EntityType<? extends Mob>> DEFAULT_MOBS = new HashMap<>();

    public Mob newZombie(EntityType<?> mobType, ServerLevel level) throws IllegalArgumentException {
        if (mobType.canSummon()) {
            if (this.isSupported(mobType)) {
                Mob mob = (Mob) mobType.create(level);
                ZombieLoadUtils.setLoadedIfAbsentMainGoal(mob);
                ZombieLoadUtils.setZombieLike(mob, this);
                return mob;
            } else
                throw new IllegalArgumentException(ComponentUtils.translate("exception.uninfectedzone.newZombie.unsupported", mobType.getDescription()).getString());
        } else
            throw new IllegalArgumentException(ComponentUtils.translate("exception.uninfectedzone.newZombie.unsummonable", mobType.getDescription()).getString());
    }

    private boolean isSupported(EntityType<?> mobType) {
        return this.getSupportedMobs().containsKey(mobType.getRegistryName());
    }

    public void load(Mob mob) {
        if (mob != null) {
            ZombieMainGoal zombieMainGoal = this.createMainGoal(mob);
            zombieMainGoal.getZombie().goalSelector.removeAllGoals();
            //zombieMainGoal.getZombie().targetSelector.removeAllGoals();
            zombieMainGoal.getZombie().goalSelector.addGoal(1, zombieMainGoal);
            this.registerAbilities(zombieMainGoal);
            this.registerGoals(zombieMainGoal);
        }
    }

    protected HashMap<ResourceLocation, EntityType<? extends Mob>> getSupportedMobs() {
        if (DEFAULT_MOBS.isEmpty()) {
            init();
        }
        return DEFAULT_MOBS;
    }

    public void equip(Mob mob) {
    }

    public static void init() {
        DEFAULT_MOBS.put(EntityType.ZOMBIE.getRegistryName(), EntityType.ZOMBIE);
        DEFAULT_MOBS.put(EntityType.ZOMBIE_VILLAGER.getRegistryName(), EntityType.ZOMBIE_VILLAGER);
        DEFAULT_MOBS.put(EntityType.HUSK.getRegistryName(), EntityType.HUSK);
        DEFAULT_MOBS.put(EntityType.PIG.getRegistryName(), EntityType.PIG);
    }

    protected abstract ZombieMainGoal createMainGoal(Mob mob);

    public abstract void registerAbilities(ZombieMainGoal zombieMainGoal);

    public abstract void registerGoals(ZombieMainGoal zombieMainGoal);

    public void attributes(Mob mob) {

    }
}
