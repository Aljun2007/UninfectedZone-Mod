package com.aljun.uninfectedzone.core.utils;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.api.registry.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.common.zombie.attribute.ZombieAttributes;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Objects;


public class ZombieUtils {

    public static final VarSet<String> ZOMBIE_LIKE = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.STRING).defaultVar("uninfectedzone:dummy").create("zombie_like");

    public static final VarSet<Boolean> LOADED = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.BOOLEAN).defaultVar(false).create("loaded");

    public static final VarSet<Boolean> MARKED_TO_LOAD = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.BOOLEAN).defaultVar(false).create("marked_to_load");

    public static boolean isMarkedToLoad(Mob mob) {
        return TagUtils.fastRead(TagUtils.getRoot(mob), MARKED_TO_LOAD);
    }

    public static void clean(Mob mob) {
        if (isLoaded(mob)) {
            mob.goalSelector.removeAllGoals();
            mob.targetSelector.removeAllGoals();
            TagUtils.fastWrite(TagUtils.getRoot(mob), LOADED, false);
        }
    }

    public static boolean isLoaded(Mob mob) {
        return TagUtils.fastRead(TagUtils.getRoot(mob), LOADED);
    }

    public static void markToLoad(Mob mob) {
        if (!isLoaded(mob)) {
            TagUtils.fastWrite(TagUtils.getRoot(mob), MARKED_TO_LOAD, true);
        }
    }

    public static void loadOrCover(Mob mob, ZombieLike zombieLike) {
        try {
            TagUtils.fastWrite(TagUtils.getRoot(mob), ZombieUtils.ZOMBIE_LIKE, Objects.requireNonNull(zombieLike.getRegistryName()).toString());
        } catch (NullPointerException e) {
            TagUtils.fastWrite(TagUtils.getRoot(mob), ZombieUtils.ZOMBIE_LIKE, "uninfectedzone:dummy");
        }
        TagUtils.fastWrite(TagUtils.getRoot(mob), ZombieUtils.LOADED, true);
    }

    public static ZombieLike getZombieLike(Mob mob) {
        ZombieLike zombieLike = UninfectedZoneRegistry.ZOMBIE_LIKES.get().getValue(new ResourceLocation(getZombieLikeID(mob)));
        return zombieLike == null ? ZombieLike.DUMMY.get() : zombieLike;
    }

    public static String getZombieLikeID(Mob mob) {
        return TagUtils.fastRead(TagUtils.getRoot(mob), ZOMBIE_LIKE);
    }

    public static boolean canAttack(LivingEntity target) {
        return true;
    }

    @Nullable
    public static ZombieMainGoal getMainGoalOrAbsent(Mob mob) {
        final ZombieMainGoal[] result = {null};
        mob.goalSelector.getAvailableGoals().forEach((goal) -> {
                    if (goal.getGoal().getClass().equals(ZombieMainGoal.class)) {
                        result[0] = (ZombieMainGoal) goal.getGoal();
                    }
                }
        );
        return result[0];
    }

    public static boolean isTargetLegal(@Nullable Entity entity) {
        if (entity == null) return false;
        boolean b = true;
        if (entity instanceof Player player) {
            b = !player.isCreative() && !player.isSpectator();
        }
        return b && entity.isAlive();
    }

    public static double getZombieHearingDistance(Mob mob) {
        return getAttributeOrDefault(mob, ZombieAttributes.SMELLING_DISTANCE.get());
    }

    public static double getAttributeOrDefault(Mob mob, Attribute attribute) {
        try {
            return mob.getAttributeValue(attribute);
        } catch (Throwable throwable) {
            return attribute.getDefaultValue();
        }
    }

    public static double getZombieDigSpeed(Mob mob) {
        return getAttributeOrDefault(mob, ZombieAttributes.DIG_SPEED.get());
    }

    public static boolean canZombieFollow(Mob mob, LivingEntity target) {
        return mob.distanceToSqr(target) <= getZombieSmellingDistance(mob) * 4;
    }

    public static double getZombieSmellingDistance(Mob mob) {
        return getAttributeOrDefault(mob, ZombieAttributes.SMELLING_DISTANCE.get());
    }
}
