package com.oasys.server.data;


import java.util.Objects;

/**
 * Represents a bet on either a winning or a losing camel.
 */
public class FinalBet {
    private int playerId;
    private int camelId;

    /**
     * Initializes an object of FinalBet.
     * @param playerId The ID of player who bets on camel.
     * @param camelId The ID of the camel who gets bet on.
     */
    public FinalBet(int playerId, int camelId) {
        this.playerId = playerId;
        this.camelId = camelId;
    }

    /**
     * Gets player's ID.
     * @return player's ID.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Sets player's ID.
     * @param playerId The ID of player who bets on camel.
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets camel's ID.
     * @return camel's ID.
     */
    public int getCamelId() {
        return camelId;
    }

    /**
     * Sets camel's ID.
     * @param camelId The ID of the camel who gets bet on.
     */
    public void setCamelId(int camelId) {
        this.camelId = camelId;
    }

    /**
     * Checks equality of another object with the final bet.
     * @param o Another object.
     * @return True iff the ID of player and camel are identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalBet finalBet = (FinalBet) o;
        return playerId == finalBet.playerId && camelId == finalBet.camelId;
    }

    /**
     * Hashes the final bet by using the player's and camel's ID.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(playerId, camelId);
    }
}
