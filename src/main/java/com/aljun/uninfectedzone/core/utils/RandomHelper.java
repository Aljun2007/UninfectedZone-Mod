package com.aljun.uninfectedzone.core.utils;

import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomHelper {
    public static final Random RANDOM = new Random();

    private static final RandomSource<Direction> HORIZONTAL_DIRECTIONS =
            new RandomSource<Direction>()
                    .add(Direction.NORTH, 1d)
                    .add(Direction.SOUTH, 1d)
                    .add(Direction.EAST, 1d)
                    .add(Direction.WEST, 1d)
                    .build();

    public static Direction randomHorizontalDirection() {
        return HORIZONTAL_DIRECTIONS.nextValue();
    }

    public static class RandomSource<T> {
        private final List<T> VALUE = new ArrayList<>();
        private final List<Double> RIGHT = new ArrayList<>();
        private double weightTotal = 0d;
        private boolean build = false;

        public static <E> RandomSource<E> create() {
            return new RandomSource<>();
        }

        public RandomSource<T> add(T value, double weight) {
            if (this.build) return this;
            else if (weight > 0) {
                if (VALUE.contains(value)) {
                    RIGHT.set(VALUE.indexOf(value), RIGHT.get(VALUE.indexOf(value)) + weight);
                } else {
                    VALUE.add(value);
                    RIGHT.add(weight);
                }
                weightTotal += weight;
            } else if (weight < 0) {
                throw new IndexOutOfBoundsException("\"weight\" > 0d, but :" + weight);
            }
            return this;
        }

        public RandomSource<T> build() {
            this.build = true;
            return this;
        }

        public T nextValue() {
            if (this.build) {
                double random = RANDOM.nextDouble(0d, weightTotal);
                double before = 0d;
                double after = 0d;
                int i = -1;
                for (double j : RIGHT) {
                    i++;
                    after += j;
                    if (before <= random && random <= after) return VALUE.get(i);
                    before += j;
                }
                return VALUE.get(0);
            } else return null;
        }
    }
}
