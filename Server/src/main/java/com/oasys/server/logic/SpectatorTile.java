package com.oasys.server.logic;

/**
 * Represents a spectator tile in the game.
 * A spectator tile has two sides: +1 and -1, which affect camel movement.
 */
public class SpectatorTile {
    private final int playerId; // The ID of the player who owns this tile.
    private final int positiveEffect = 1; // The positive effect (+1 movement).
    private final int negativeEffect = -1; // The negative effect (-1 movement).

    /**
     * Constructor for creating a spectator tile for a player.
     *
     * @param playerId The ID of the player who owns this tile.
     */
    public SpectatorTile(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the player ID associated with this tile.
     *
     * @return The player ID.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Applies the positive effect of the tile.
     *
     * @return The value of the positive effect.
     */
    public int applyPositiveEffect() {
        return positiveEffect;
    }

    /**
     * Applies the negative effect of the tile.
     *
     * @return The value of the negative effect.
     */
    public int applyNegativeEffect() {
        return negativeEffect;
    }

    @Override
    public String toString() {
        return "SpectatorTile{" +
                "playerId=" + playerId +
                ", positiveEffect=" + positiveEffect +
                ", negativeEffect=" + negativeEffect +
                '}';
    }
}
