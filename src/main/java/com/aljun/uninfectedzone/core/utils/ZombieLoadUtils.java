package com.aljun.uninfectedzone.core.utils;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.forgeRegister.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import com.aljun.uninfectedzone.deafult.zombie.zombieLikes.ZombieLikes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

import java.util.Objects;

public class ZombieLoadUtils {

    public static final VarSet<String> ZOMBIE_LIKE = VarSet.builder(UninfectedZone.MOD_ID, TagUtils.TagType.STRING).defaultVar("dummy").create("zombie_like");

    public static final VarSet<Boolean> LOADED = VarSet.builder(UninfectedZone.MOD_ID, TagUtils.TagType.BOOLEAN).defaultVar(false).create("loaded");


    public static boolean hasLoadedIfAbsentMainGoal(Mob mob) {
        return TagUtils.fastRead(TagUtils.getRoot(mob), LOADED);
    }

    public static void setLoadedIfAbsentMainGoal(Mob mob) {
        TagUtils.fastWrite(TagUtils.getRoot(mob), LOADED, true);
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
        return zombieLike == null ? ZombieLikes.DUMMY.get() : zombieLike;
    }

    public static String getZombieLikeID(Mob mob) {
        return TagUtils.fastRead(TagUtils.getRoot(mob), ZOMBIE_LIKE);
    }
}
