package com.oasys.engine.data;

import com.google.gson.annotations.SerializedName;

/**
 * The possible states of a player:
 * PLAYING: the player is playing(default)
 * CURRENT_STAGE_FORFEIT: the player is excluded from scoring in current stage.
 * GAME_FORFEIT: the player is excluded from entire game. Acts only as passive observer.
 * DISCONNECTED: the player is disconnected from game and is no part of it anymore.
 */
public enum PlayerState {
    @SerializedName("playing")
    PLAYING,
    @SerializedName("currentStageForfeit")
    CURRENT_STAGE_FORFEIT,
    @SerializedName("gameForfeit")
    GAME_FORFEIT,
    @SerializedName("disconnected")
    DISCONNECTED
}
