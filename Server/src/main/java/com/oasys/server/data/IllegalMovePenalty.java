package com.oasys.server.data;

import com.google.gson.annotations.SerializedName;

/**
 * The penalty for a player that does none or an invalid move.
 */
public enum IllegalMovePenalty {
    @SerializedName("forfeitCurrentStage")
    FORFEIT_CURRENT_STAGE,
    @SerializedName("forfeitGame")
    FORFEIT_GAME
}