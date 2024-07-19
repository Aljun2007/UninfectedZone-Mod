package com.aljun.uninfectedzone.core.threads;

import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ZombiePathCreating {


    private static final List<CreatingFigures> figures = new ArrayList<>();
    private static final List<CreatingFigures> toAdd = new ArrayList<>();
    private static int id = 0;

    public static void tick() {
        if (!toAdd.isEmpty()) {
            figures.addAll(toAdd);
            toAdd.clear();
        }
        if (!figures.isEmpty()) {
            int i1 = figures.size();
            ArrayList<CreatingFigures> out = new ArrayList<>();
            while (i1 > 0) {
                out.add(figures.get(--i1));
                if (out.size() == packSize() || i1 == 0) {
                    newThread(new ArrayList<>(out));
                    out.clear();
                }
            }
            figures.clear();
        }
    }

    private static int packSize() {
        return 20;
    }

    private static void newThread(ArrayList<CreatingFigures> figures) {
        PathCreatingThread thread = new PathCreatingThread("PathCreatingThread - " + ++id, figures.stream());
        thread.start();
    }

    public interface User {
        void receive(@Nullable Path path, int key);

        PathNavigation navigation();

        default void createPath(double wantedX, double wantedY, double wantedZ, int distance, int key) {
            toAdd.add(new CreatingFigures(this, wantedX, wantedY, wantedZ, distance, key));
        }
    }

    private static class CreatingFigures {
        public final int KEY;
        private final User USER;
        private final double WANTED_X;
        private final double WANTED_Y;
        private final double WANTED_Z;
        private final int DISTANCE;

        private CreatingFigures(User user, double wantedX, double wantedY, double wantedZ, int distance, int key) {
            this.USER = user;
            this.WANTED_X = wantedX;
            this.WANTED_Y = wantedY;
            this.WANTED_Z = wantedZ;
            this.DISTANCE = distance;
            this.KEY = key;
        }
    }

    private static class PathCreatingThread extends UninfectedZoneThread {

        private final Stream<CreatingFigures> toDo;

        private PathCreatingThread(String name, Stream<CreatingFigures> toDo) {
            super(UninfectedZoneThread.DUMMY_RUNNABLE, name);
            this.toDo = toDo;
            this.setRunnable(this::deal);
        }

        private void deal() {
            toDo.forEach((figure) -> {
                Path path = figure.USER.navigation().createPath(figure.WANTED_X, figure.WANTED_Y, figure.WANTED_Z, figure.DISTANCE);
                figure.USER.receive(path, figure.KEY);
            });
        }
    }
}
