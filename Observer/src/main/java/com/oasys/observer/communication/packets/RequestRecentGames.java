package com.oasys.observer.communication.packets;

import java.util.Objects;

/**
 * This message is sent from client to server, becauses the client requests the RecentGames Packet from the server.
 */
public class RequestRecentGames extends Packet {
    private int numGames;

    /**
     * Initializes an object of RecentGamesReq.
     * @param numGames Is the number of games that client wants to get from server.
     */
    public RequestRecentGames(int numGames) throws IllegalArgumentException {
        if (numGames < 1) {
            throw new IllegalArgumentException("numGames must be greater or equal to one.");
        } else {
            this.numGames = numGames;
        }
    }

    /**
     * Gets NumGames.
     * @return NumGames.
     */
    public int getNumGames() {
        return this.numGames;
    }

    /**
     * Sets NumGames.
     * @param numGames Must be greater or equal to one.
     */
    public void setNumGames(int numGames) throws IllegalArgumentException {
        if (numGames < 1) {
            throw new IllegalArgumentException("numGames must be greater or equal to one.");
        } else {
            this.numGames = numGames;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestRecentGames that = (RequestRecentGames) o;
        return numGames == that.numGames;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numGames);
    }
}
