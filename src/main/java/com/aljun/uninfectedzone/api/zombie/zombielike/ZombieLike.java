package com.aljun.uninfectedzone.api.zombie.zombielike;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.TagUtils;
import com.aljun.uninfectedzone.core.utils.ZombieUtils;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class ZombieLike extends ForgeRegistryEntry<ZombieLike> {
    private static final HashMap<ResourceLocation, EntityType<? extends Mob>> DEFAULT_MOBS = new HashMap<>();
    public static Supplier<ZombieLike> DUMMY;

    public Mob newZombie(EntityType<?> mobType, ServerLevel level) throws IllegalArgumentException {
        if (mobType.canSummon()) {
            if (this.isSupported(mobType)) {
                Mob mob = (Mob) mobType.create(level);
                if (mob != null) {
                    TagUtils.fastWrite(TagUtils.getRoot(mob), ZombieUtils.LOADED, true);
                    try {
                        TagUtils.fastWrite(TagUtils.getRoot(mob), ZombieUtils.ZOMBIE_LIKE, Objects.requireNonNull(this.getRegistryName()).toString());
                    } catch (NullPointerException e) {
                        TagUtils.fastWrite(TagUtils.getRoot(mob), ZombieUtils.ZOMBIE_LIKE, UninfectedZone.MOD_ID + ":dummy");
                    }
                }
                return mob;
            } else
                throw new IllegalArgumentException(ComponentUtils.translate("exception.uninfectedzone.newZombie.unsupported", mobType.getDescription()).getString());
        } else
            throw new IllegalArgumentException(ComponentUtils.translate("exception.uninfectedzone.newZombie.unsummonable", mobType.getDescription()).getString());
    }

    private boolean isSupported(EntityType<?> mobType) {
        return this.getSupportedMobs().containsKey(mobType.getRegistryName());
    }

    protected HashMap<ResourceLocation, EntityType<? extends Mob>> getSupportedMobs() {
        if (DEFAULT_MOBS.isEmpty()) {
            init();
        }
        return DEFAULT_MOBS;
    }

    public static void init() {
        DEFAULT_MOBS.put(EntityType.ZOMBIE.getRegistryName(), EntityType.ZOMBIE);
        DEFAULT_MOBS.put(EntityType.ZOMBIE_VILLAGER.getRegistryName(), EntityType.ZOMBIE_VILLAGER);
        DEFAULT_MOBS.put(EntityType.HUSK.getRegistryName(), EntityType.HUSK);
        DEFAULT_MOBS.put(EntityType.PIG.getRegistryName(), EntityType.PIG);
    }

    public void loadOrCover(Mob mob) {
        if (mob != null) {
            ZombieMainGoal zombieMainGoal = this.createMainGoal(mob);
            zombieMainGoal.getZombie().goalSelector.removeAllGoals();
            //zombieMainGoal.getZombie().targetSelector.removeAllGoals();
            zombieMainGoal.getZombie().goalSelector.addGoal(1, zombieMainGoal);
            this.registerAbilities(zombieMainGoal);
            this.registerGoals(zombieMainGoal);
            zombieMainGoal.stopInitialization();
        }
    }

    protected abstract ZombieMainGoal createMainGoal(Mob mob);

    protected abstract void registerAbilities(ZombieMainGoal zombieMainGoal);

    protected abstract void registerGoals(ZombieMainGoal zombieMainGoal);

    public void weaponAndAttribute(Mob mob) {
        this.weapon(mob);
        this.attributes(mob);
    }

    protected void weapon(Mob mob) {
    }

    protected void attributes(Mob mob) {

    }
}
