package com.aljun.uninfectedzone.core.utils;

import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomHelper {
    public static final Random RANDOM = new Random();

    private static final RandomPool<Direction> HORIZONTAL_DIRECTIONS =
            RandomPool.builder(Direction.class)
                    .add(Direction.NORTH, 1d)
                    .add(Direction.SOUTH, 1d)
                    .add(Direction.EAST, 1d)
                    .add(Direction.WEST, 1d)
                    .build();

    public static Direction randomHorizontalDirection() {
        return HORIZONTAL_DIRECTIONS.nextValue();
    }

    public static class RandomPool<T> {
        private final List<T> VAR;
        private final List<Double> WEIGHT;
        private double weightTotal;

        public RandomPool(List<T> var, List<Double> weight, double weightTotal) {
            this.VAR = var;
            this.WEIGHT = weight;
        }

        public static <T> Builder<T> builder(Class<T> directionClass) {
            return new Builder<>();
        }

        public T nextValue() {
            double random = RANDOM.nextDouble(0d, weightTotal);
            double before = 0d;
            double after = 0d;
            int i = -1;
            for (double j : WEIGHT) {
                i++;
                after += j;
                if (before <= random && random <= after) return VAR.get(i);
                before += j;
            }
            return VAR.get(0);
        }

        public static class Builder<T> {
            private final List<T> VALUE = new ArrayList<>();
            private final List<Double> WEIGHT = new ArrayList<>();
            private double weightTotal = 0d;

            public RandomPool<T> build() {
                return new RandomPool<>(VALUE, WEIGHT, weightTotal);
            }

            public Builder<T> add(T value, double weight) {
                if (weight > 0) {
                    if (VALUE.contains(value)) {
                        WEIGHT.set(VALUE.indexOf(value), WEIGHT.get(VALUE.indexOf(value)) + weight);
                    } else {
                        VALUE.add(value);
                        WEIGHT.add(weight);
                    }
                    weightTotal += weight;
                } else if (weight < 0) {
                    throw new IndexOutOfBoundsException("\"weight\" > 0d, but :" + weight);
                }
                return this;
            }
        }
    }
}
