package com.aljun.uninfectedzone.api.game;

import com.aljun.uninfectedzone.core.game.GameUtils;
import com.aljun.uninfectedzone.core.game.mode.GameMode;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class UninfectedZoneGameAPI {
    @Nullable
    public static GameMode getGameMode() {
        return GameUtils.getGameMode();
    }
}
