package com.aljun.uninfectedzone.core.zombie.goal;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.api.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.api.zombie.abilities.ZombieAbilityInstance;
import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.core.zombie.awareness.ZombieAwareness;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

public class ZombieMainGoal extends Goal {
    public static final Logger LOGGER = LogUtils.getLogger();
    private final Mob mob;
    private final HashMap<String, ZombieAbilityInstance<?>> abilities = new HashMap<>();
    private final ZombieAwareness AWARENESS;
    private final ZombieLike ZOMBIE_LIKE;
    private boolean isInInitialization = true;

    public ZombieMainGoal(Mob zombie, Function<ZombieMainGoal, ZombieAwareness> awarenessFunction, ZombieLike zombieLike) {
        this.mob = zombie;
        this.AWARENESS = awarenessFunction.apply(this);
        this.ZOMBIE_LIKE = zombieLike;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.AWARENESS.tick();
        this.abilities.forEach((key, ability) -> ability.tick());
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends ZombieAbility> ZombieAbilityInstance<T> getAbilityInstanceOrAbsent(T ability) {
        if (!this.hasAbility(ability)) return null;
        try {
            ZombieAbilityInstance<?> result = abilities.getOrDefault(Objects.requireNonNull(ability.getRegistryName()).toString(), null);
            if (result.is(ability))
                return (ZombieAbilityInstance<T>) result;
            else
                throw new IllegalArgumentException("The ability \"" + ability.getRegistryName() + "\" is unexpected.\n Result: " + result.getClass() + " , Input: " + ability.getClass());
        } catch (NullPointerException e) {
            return null;
        }
    }

    public boolean hasAbility(ZombieAbility ability) {
        try {
            return abilities.containsKey(Objects.requireNonNull(ability.getRegistryName()).toString());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void stopInitialization() {
        if (this.isInInitialization) {
            this.loadAbilitiesFromTag();
            this.isInInitialization = false;
        }
    }

    public void loadAbilitiesFromTag() {
        CompoundTag tag = this.mob.getPersistentData();
        if (tag.contains(UninfectedZone.MOD_ID) && tag.getTagType("abilities") == Tag.TAG_COMPOUND) {
            tag = tag.getCompound(UninfectedZone.MOD_ID);
            if (tag.contains("abilities") && tag.getTagType("abilities") == Tag.TAG_LIST) {
                ListTag abilityListTag = tag.getList("abilities", Tag.TAG_COMPOUND);
                abilityListTag.forEach((tag1 -> {
                    if (tag1 instanceof CompoundTag compoundTag) {
                        String id = compoundTag.getString("id");
                        if (this.abilities.containsKey(id)) {
                            ZombieAbilityInstance<?> abilityInstance = this.abilities.get(id);
                            abilityInstance.loadFromTag(compoundTag);
                        }
                    }
                }));
            }
        }
    }

    public void addAbility(ZombieAbility ability) {
        if (this.isInInitialization) {
            try {
                this.abilities.put(Objects.requireNonNull(ability.getRegistryName()).toString(), ability.createInstance(this));
            } catch (NullPointerException e) {
                LOGGER.error("Ability adding failed : {}", e.toString());
            }
        }
    }

    public ZombieAwareness getAwareness() {
        return this.AWARENESS;
    }

    public boolean isAlive() {
        return this.getZombie().isAlive();
    }

    public Mob getZombie() {
        return this.mob;
    }

    public void saveAbilitiesAsTag() {

        CompoundTag tag = this.mob.getPersistentData();
        if (!tag.contains(UninfectedZone.MOD_ID) || tag.getTagType("abilities") != Tag.TAG_COMPOUND) {
            tag.put(UninfectedZone.MOD_ID, new CompoundTag());
        }
        tag = tag.getCompound(UninfectedZone.MOD_ID);

        ListTag abilityListTag;
        if (!tag.contains("abilities") || tag.getTagType("abilities") != Tag.TAG_LIST) {
            tag.put("abilities", new ListTag());
        }

        abilityListTag = tag.getList("abilities", Tag.TAG_COMPOUND);
        this.abilities.forEach((id, ability) -> {
            CompoundTag tag1 = ability.saveAsTag();
            tag1.put("id", StringTag.valueOf(id));
            abilityListTag.addTag(abilityListTag.size(), tag1);
        });
    }

    public void broadcast(BroadcastType broadcastType) {
        this.abilities.forEach((key, ability) -> ability.receiveBroadcast(broadcastType));
        this.mob.goalSelector.getAvailableGoals().forEach((g) -> {
            if (g.getGoal() instanceof BroadcastReceiver receiver) {
                receiver.receiveBroadcast(broadcastType);
            }
        });
        this.mob.targetSelector.getAvailableGoals().forEach((g) -> {
            if (g.getGoal() instanceof BroadcastReceiver receiver) {
                receiver.receiveBroadcast(broadcastType);
            }
        });
    }

    public ZombieLike getZombieLike() {
        return this.ZOMBIE_LIKE;
    }

    public interface BroadcastReceiver {
        default void receiveBroadcast(BroadcastType broadcastType) {
        }
    }

    public static class BroadcastType {
        private static int id = 0;
        public static final BroadcastType HURT = create();
        public static final BroadcastType DEATH = create();
        public final int ID;

        private BroadcastType(int i) {
            this.ID = i;
        }

        public static BroadcastType create() {
            return new BroadcastType(++id);
        }

        public boolean is(BroadcastType broadcastType) {
            return this.ID == broadcastType.ID;
        }
    }

}
