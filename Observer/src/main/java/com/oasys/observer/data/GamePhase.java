package com.oasys.observer.data;

import com.google.gson.annotations.SerializedName;

/**
 * The possible phases of a game:
 * 1. created: The game was created but not started
 * 2. playing: The current player must make a move
 * 3. visualizing: The clients currently visualize the move
 * 4. paused: The game is paused and will be continued in the future
 * 5. finished: The game has ended or was canceled
 */
public enum GamePhase {
    @SerializedName("created")
    CREATED,
    @SerializedName("playing")
    PLAYING,
    @SerializedName("visualizing")
    VISUALIZING,
    @SerializedName("paused")
    PAUSED,
    @SerializedName("finished")
    FINISHED
}
