package com.aljun.uninfectedzone.core.zombie.goal;

import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbilityInstance;
import com.aljun.uninfectedzone.core.zombie.awareness.ZombieAwareness;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import com.aljun.uninfectedzone.core.zombie.utils.ZombieMoveControl;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.Function;

public class ZombieMainGoal extends Goal {
    private final Mob ZOMBIE;
    private final HashMap<String, ZombieAbilityInstance<?>> abilities = new HashMap<>();
    private final ZombieAwareness AWARENESS;
    private final ZombieMoveControl MOVE_CONTROL;
    private boolean isInInitialization = true;

    public ZombieMainGoal(Mob zombie, Function<ZombieMainGoal, ZombieAwareness> awarenessFunction, Function<ZombieMainGoal, ZombieMoveControl> moveControlFunction) {
        this.ZOMBIE = zombie;
        this.AWARENESS = awarenessFunction.apply(this);
        this.MOVE_CONTROL = moveControlFunction.apply(this);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }


    @Override
    public void tick() {
        this.abilities.forEach((key, ability) -> ability.tick());
        this.AWARENESS.tick();
        this.MOVE_CONTROL.tick();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends ZombieAbility> ZombieAbilityInstance<T> getAbilityInstanceOrAbsent(T ability) {
        if (!this.hasAbility(ability)) return null;
        ZombieAbilityInstance<?> result = abilities.getOrDefault(ability.getKey(), null);
        if (result.is(ability))
            return (ZombieAbilityInstance<T>) result;
        else
            throw new IllegalArgumentException("The ability \"" + ability.getKey() + "\" is unexpected.\n Result: " + result.getClass() + " , Input: " + ability.getClass());

    }

    public boolean hasAbility(ZombieAbility ability) {
        return abilities.containsKey(ability.getKey());
    }

    public void load(ZombieLike zombieLike) {
        if (this.isInInitialization) {
            this.isInInitialization = false;
        }
    }

    public void addAbility(ZombieAbility ability) {
        if (this.isInInitialization) {
            this.abilities.put(ability.getKey(), ability.createInstance(this));
        }
    }

    public ZombieAwareness getAwareness() {
        return this.AWARENESS;
    }

    public ZombieMoveControl getMoveControl() {
        return this.MOVE_CONTROL;
    }

    public boolean isAlive() {
        return this.getZombie().isAlive();
    }

    public Mob getZombie() {
        return this.ZOMBIE;
    }

    public void zombieAttack() {

    }


}
