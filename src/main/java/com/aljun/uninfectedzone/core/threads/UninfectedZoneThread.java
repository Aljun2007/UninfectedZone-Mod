package com.aljun.uninfectedzone.core.threads;

public class UninfectedZoneThread extends Thread {
    public static final Runnable DUMMY_RUNNABLE = () -> {
    };
    public final String NAME;
    protected Runnable RUNNABLE;

    UninfectedZoneThread(Runnable runnable, String name) {
        this.RUNNABLE = runnable;
        NAME = name;
    }

    public Runnable getRunnable() {
        return RUNNABLE;
    }

    public void setRunnable(Runnable RUNNABLE) {
        this.RUNNABLE = RUNNABLE;
    }

    public void run() {
        this.RUNNABLE.run();
    }

}
