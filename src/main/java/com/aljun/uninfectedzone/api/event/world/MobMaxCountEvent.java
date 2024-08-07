package com.aljun.uninfectedzone.api.event.world;

import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.Event;

public class MobMaxCountEvent extends Event {
    private final MobCategory MOB_CATEGORY;
    private int maxCount;
    private boolean isDirty = false;

    public MobMaxCountEvent(MobCategory category) {
        MOB_CATEGORY = category;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public MobCategory getMobCategory() {
        return MOB_CATEGORY;
    }

    public int getMaxCount() {
        return this.isDirty ? this.maxCount : this.MOB_CATEGORY.getMaxInstancesPerChunk();
    }

    public boolean setMaxCount(int max) {
        if (max >= 0) {
            this.isDirty = true;
            this.maxCount = max;
            return true;
        } else return false;
    }

    public void setDefault() {
        this.isDirty = false;
        this.maxCount = this.MOB_CATEGORY.getMaxInstancesPerChunk();
    }

    public boolean isDirty() {
        return this.isDirty;
    }
}
