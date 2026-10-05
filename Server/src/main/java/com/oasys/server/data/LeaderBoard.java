package com.oasys.server.data;

import com.oasys.server.data.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * The class leaderboard gives you a list of all the players in the game sorted by their win in reversed order.
 *It begins with player with the most money up to the player with the least money.
 */
public class LeaderBoard {
    private List<Player> players;

    /**
     * Initializes an empty object of LeaderBoard.
     */
    public LeaderBoard() {
        this.players = new ArrayList<>();
    }

    /**
     * Adds a player to list players in LeaderBoard.
     * @param player The player gets added to the list of players.
     */
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    /**
     * Sorts the list in order of the gained money.
     * From player with most money up to player with least money.
     */
    public void sortPlayers() {
        players.sort(Comparator.comparingInt(Player::getMoney).reversed());
    }

    /**
     * Prints an output of ranking of the players.
     */
    public void displayLeaderboard() {
        System.out.println("Leaderboard:");
        int rank = 1;
        for (Player player : players) {
            System.out.println(rank + ". " + player.getName() + " has won: " + player.getMoney());
            rank++;
        }
    }

    /**
     * Gets players list.
     * @return players. This list is sorted by the win of the players. From player with the most money to
     * Player with the least money.
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Sets player list.
     * @param players This list is sorted by the win of the players. From player with the most money to
     * Player with the least money.
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderBoard that = (LeaderBoard) o;
        return Objects.equals(players, that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(players);
    }
}

