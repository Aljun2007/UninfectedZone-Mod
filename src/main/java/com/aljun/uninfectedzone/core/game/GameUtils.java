package com.aljun.uninfectedzone.core.game;

import com.aljun.uninfectedzone.core.game.mode.GameMode;

public class GameUtils {

    private static GameMode gameMode;

    public static boolean isVanillaInfectionDisabled() {
        return true;
    }

    public static boolean isUninfectedZoneInfectionDisabled() {
        return false;
    }

    public static GameMode getGameMode() {
        return gameMode;
    }

}
