package com.aljun.uninfectedzone.core.utils;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.registry.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.util.Objects;


public class ZombieUtils {

    public static final VarSet<String> ZOMBIE_LIKE = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.STRING).defaultVar("dummy").create("zombie_like");

    public static final VarSet<Boolean> LOADED = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.BOOLEAN).defaultVar(false).create("loaded");


    public static boolean hasLoadedIfAbsentMainGoal(Mob mob) {
        return TagUtils.fastRead(TagUtils.getRoot(mob), LOADED);
    }

    public static void setZombieLike(Mob mob, ZombieLike zombieLike) {
        try {
            TagUtils.fastWrite(TagUtils.getRoot(mob), ZOMBIE_LIKE, Objects.requireNonNull(zombieLike.getRegistryName()).toString());
        } catch (NullPointerException e) {
            TagUtils.fastWrite(TagUtils.getRoot(mob), ZOMBIE_LIKE, UninfectedZone.MOD_ID + ":dummy");
        }
    }

    public static ZombieLike getZombieLike(Mob mob) {
        ZombieLike zombieLike = UninfectedZoneRegistry.ZOMBIE_LIKES.get().getValue(new ResourceLocation(getZombieLikeID(mob)));
        return zombieLike == null ? ZombieLike.DUMMY.get() : zombieLike;
    }

    public static String getZombieLikeID(Mob mob) {
        return TagUtils.fastRead(TagUtils.getRoot(mob), ZOMBIE_LIKE);
    }

    public static boolean canBeAttack(LivingEntity target) {
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
}
