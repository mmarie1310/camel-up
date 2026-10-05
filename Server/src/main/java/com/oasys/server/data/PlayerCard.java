package com.oasys.server.data;

import java.util.Objects;

/**
 * A player card on a board space makes a camel move an additional space in the front or back.
 */
public class PlayerCard {
    private int playerId;
    private int spacesMoved;

    /**
     * Initializes a PlayerCard.
     * @param playerId The ID of the player who placed the card. Must be non-negative.
     * @param spacesMoved The number of spaces the camel is moved when stepping on the space. Must be either -1 or 1.
     * @throws IllegalArgumentException If the playerID is negative.
     * @throws IllegalArgumentException If the amount of moved spaces is not -1 or 1.
     */
    public PlayerCard(int playerId, int spacesMoved) throws IllegalArgumentException {
        if (playerId < 0) {
            throw new IllegalArgumentException("playerId must be non-negative");
        }
        if (spacesMoved != -1 && spacesMoved != 1) {
            throw new IllegalArgumentException("spacesMoved can only be 1 or -1");
        }
        this.playerId = playerId;
        this.spacesMoved = spacesMoved;
    }

    /**
     * Gets the ID of the player who placed the card.
     * @return The ID of the player who placed the card.
     */
    public int getPlayerId() {
        return this.playerId;
    }

    /**
     * Sets the ID of the player who placed the card.
     * @param playerId The ID of the player who placed the card. Must be non-negative.
     * @throws IllegalArgumentException If the playerId is negative.
     */
    public void setPlayerId(int playerId) throws IllegalArgumentException {
        if (playerId < 0) {
            throw new IllegalArgumentException("playerId must be non-negative");
        }
        this.playerId = playerId;
    }

    /**
     * Gets the number of moved spaces when a camel steps on the space.
     * @return The number of moved spaces when a camel steps on the space.
     */
    public int getSpacesMoved() {
        return this.spacesMoved;
    }

    /**
     * Sets the number of moved spaces when a camel steps on the space.
     * @param spacesMoved The number of moved spaces when a camel steps on the space. Must be either -1 or 1.
     * @throws IllegalArgumentException  If the amount of moved spaces is not -1 or 1.
     */
    public void setSpacesMoved(int spacesMoved) throws IllegalArgumentException {
        if (spacesMoved != -1 && spacesMoved != 1) {
            throw new IllegalArgumentException("spacesMoved can only be 1 or -1");
        }
        this.spacesMoved = spacesMoved;
    }

    /**
     * Checks equality of another object with the PlayerCard.
     * @param o Another object.
     * @return True iff the player and amount of moved spaces are identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCard that = (PlayerCard) o;
        return getPlayerId() == that.getPlayerId() && getSpacesMoved() == that.getSpacesMoved();
    }

    /**
     * Hashes the PlayerCard by using the player's ID and the amount of moved spaces.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getPlayerId(), getSpacesMoved());
    }
}