package com.aljun.uninfectedzone.core.debug;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.utils.TagUtils;
import com.aljun.uninfectedzone.core.utils.VarSet;

public class DEBUG {
    public static final VarSet<Boolean> KILLER_ITEM = VarSet.builder(UninfectedZone.MOD_ID, TagUtils.TagType.BOOLEAN).next("debug").defaultVar(false).create("killer");
    public static final VarSet<Boolean> SNATCHER_ITEM = VarSet.builder(UninfectedZone.MOD_ID, TagUtils.TagType.BOOLEAN).next("debug").defaultVar(false).create("snatcher");
    public static final VarSet<Boolean> HEAL_ITEM = VarSet.builder(UninfectedZone.MOD_ID, TagUtils.TagType.BOOLEAN).next("debug").defaultVar(false).create("heal");
    public static final VarSet<Boolean> DAY_ITEM = VarSet.builder(UninfectedZone.MOD_ID, TagUtils.TagType.BOOLEAN).next("debug").defaultVar(false).create("day");
    public static final VarSet<Boolean> NIGHT_ITEM = VarSet.builder(UninfectedZone.MOD_ID, TagUtils.TagType.BOOLEAN).next("debug").defaultVar(false).create("night");
}
