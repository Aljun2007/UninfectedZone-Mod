package com.aljun.uninfectedzone.api.event.zombie;

import com.aljun.uninfectedzone.core.game.mode.GameMode;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;


public class ZombieAttributeModifyEvent extends MobEvent {
    private final double defaultValue;
    private final Attribute attribute;
    private final GameMode gameMode;
    private boolean isDirty = false;
    private double value;

    public ZombieAttributeModifyEvent(GameMode gameMode, Mob mob, double defaultValue, Attribute attribute) {
        super(mob);
        this.attribute = attribute;
        this.defaultValue = defaultValue;
        this.gameMode = gameMode;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public double getValue() {
        return this.isDirty ? this.value : this.defaultValue;
    }

    public void set(double value) {
        this.isDirty = true;
        this.value = value;
    }

    public void setDefault() {
        this.isDirty = false;
        this.value = this.defaultValue;
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }
}
