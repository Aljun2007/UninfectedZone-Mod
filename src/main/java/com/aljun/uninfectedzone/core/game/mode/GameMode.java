package com.aljun.uninfectedzone.core.game.mode;

import com.aljun.uninfectedzone.core.event.provider.UninfectedZoneEventFactory;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class GameMode {
    public static final Supplier<Builder> DISABLED;

    static {
        Builder mode = new Builder(DisabledMode::new);
        DISABLED = () -> mode;
    }

    public final ResourceLocation name;

    protected GameMode(Builder builder) {
        this.name = builder.getRegistryName();
    }

    public final double modifyAndPostEvent(double value, Attribute attribute, Mob mob) {
        return UninfectedZoneEventFactory.onModifyingAttributes(this, mob, this.modify(value, attribute, mob), attribute).getValue();
    }

    protected abstract double modify(double value, Attribute attribute, Mob mob);


    public abstract void load(JsonObject dataJson);

    public abstract void save(JsonObject dataJson);

    private static class DisabledMode extends GameMode {

        protected DisabledMode(Builder builder) {
            super(builder);
        }

        @Override
        protected double modify(double value, Attribute attribute, Mob mob) {
            return value;
        }

        @Override
        public void load(JsonObject dataJson) {

        }

        @Override
        public void save(JsonObject dataJson) {

        }
    }

    public static class Builder extends ForgeRegistryEntry<Builder> {

        private final Function<Builder, GameMode> gameModeSupplier;

        public Builder(Function<Builder, GameMode> gameModeSupplier) {
            this.gameModeSupplier = gameModeSupplier;
        }


        public GameMode get() {
            return gameModeSupplier.apply(this);
        }
    }
}
