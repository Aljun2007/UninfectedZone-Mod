package com.aljun.uninfectedzone.core.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Register Config Factory
 *
 * @author Aljun2007
 */

public class ConfigRegister {
    static List<Runnable> toRegister = new ArrayList<>();

    public static <T> Supplier<ConfigSet<T>> registerDelay(UninfectedZoneConfig.Builder<T> builder) {
        DelaySupplier<ConfigSet<T>> delaySupplier = new DelaySupplier<>();
        toRegister.add(() -> delaySupplier.receive(UninfectedZoneConfig.register(builder)));
        return delaySupplier;
    }

    static void registerDelay() {
        toRegister.forEach(Runnable::run);
    }

    private static class DelaySupplier<T> implements Supplier<T> {

        private T t = null;

        public void receive(T t) {
            this.t = t;
        }

        @Override
        public T get() {
            return this.t;
        }
    }
}
