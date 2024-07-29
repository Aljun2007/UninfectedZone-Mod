package com.aljun.uninfectedzone.core.utils;

public class LogicalUtils {
    public static boolean isClient = false;

    public static boolean isClient() {
        return isClient;
    }

    public static boolean isServer() {
        return !isClient;
    }
}
