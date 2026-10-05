package com.oasys.server.communication.packets;

import com.oasys.server.data.Lobby;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Is sent from server to client when game was ended.
 */
public class GameEnd extends Packet {
    Lobby lobby;
    ArrayList<Integer> leaderboard;

    /**
     * Initializes an object of GameEnd.
     * @param lobby Last update of lobby.
     * @param leaderboard An array list of players sorted by their money.
     *                    From the player with the most money to the player with the least money.
     */
    public GameEnd(Lobby lobby, ArrayList<Integer> leaderboard) {
        this.lobby = lobby;
        this.leaderboard = leaderboard;
    }

    /**
     * Get lobby.
     * @return lobby.
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     * Set lobby.
     * @param lobby Last update of lobby.
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Gets leaderboard.
     * @return leaderboard.
     */
    public ArrayList<Integer> getLeaderboard() {
        return leaderboard;
    }

    /**
     * Set leaderboard.
     * @param leaderboard An array list of players sorted by their money.
     *                    From the player with the most money to the player with the least money.
     */
    public void setLeaderboard(ArrayList<Integer> leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEnd gameEnd = (GameEnd) o;
        return Objects.equals(lobby, gameEnd.lobby) && Objects.equals(leaderboard, gameEnd.leaderboard);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lobby);
    }


}
