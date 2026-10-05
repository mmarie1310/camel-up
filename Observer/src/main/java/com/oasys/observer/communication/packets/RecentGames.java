package com.oasys.observer.communication.packets;

import com.oasys.observer.data.RecentGame;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Contains a list of finished games.
 */
public class RecentGames extends Packet {
    private ArrayList<RecentGame> lobbies;

    /**
     * Initializes an object of RecentGames.
     * @param lobbies Contains a list of finished games.
     */
    public RecentGames(ArrayList<RecentGame> lobbies) {
        this.lobbies = lobbies;
    }

    /**
     * Gets lobbies.
     * @return Lobbies.
     */
    public ArrayList<RecentGame> getLobbies() {
        return this.lobbies;
    }

    /**
     * Sets lobbies.
     * @param lobbies Are list of finished games.
     */
    public void setLobbies(ArrayList<RecentGame> lobbies) {
        this.lobbies = lobbies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecentGames that = (RecentGames) o;
        return Objects.equals(lobbies, that.lobbies);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lobbies);
    }
}
