package com.oasys.observer.data;

import java.util.Objects;

/**
 * A participant that is in a lobby.
 */
public class LobbyParticipant {
    private int playerId;
    private String name;

    /**
     * Initializes a player.
     * @param playerId The unique identifier of a player. Must be non-negative.
     * @param name The name of the player.
     * @throws IllegalArgumentException If the playerId is negative.
     */
    public LobbyParticipant(int playerId, String name) throws IllegalArgumentException {
        if (playerId < 0) {
            throw new IllegalArgumentException("player ID must be non-negative");
        }

        this.playerId = playerId;
        this.name = name;
    }

    /**
     * Gets the player's unique identifier.
     * @return The player's unique identifier.
     */
    public int getPlayerId() {
        return this.playerId;
    }

    /**
     * Sets the player's unique identifier. Must be non-negative
     * @param playerId The player's unique identifier.
     * @throws IllegalArgumentException If the playerId is negative.
     */
    public void setPlayerId(int playerId) throws IllegalArgumentException {
        if (playerId < 0) {
            throw new IllegalArgumentException("player ID must be non-negative");
        }

        this.playerId = playerId;
    }

    /**
     * Gets the player's name.
     * @return The player's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the player's name.
     * @param name The player's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks equality of another object with the player.
     * @param o Another object.
     * @return True iff the ID and name of the player are identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LobbyParticipant player = (LobbyParticipant) o;
        return this.getPlayerId() == player.getPlayerId() && this.getName().equals(player.getName());
    }

    /**
     * Hashes the player by using the player's ID and name.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getPlayerId(), this.getName());
    }

    /**
     * Converts the player into a string i.e. its name.
     * @return The name of the player.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
