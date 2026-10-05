package com.oasys.engine.data;

import com.oasys.engine.communication.packets.GameState;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents an already finished game.
 */
public class RecentGame {
    public GameState gameState;
    public ArrayList<Integer> leaderboard;

    /**
     * Initializes an object of RecentGame.
     * @param gameState The last update of the game state of the finished game.
     * @param leaderboard An array list of the players sorted by their amount of money from player with the most
     *                    to the player with the least amount of money.
     */
    public RecentGame(GameState gameState, ArrayList<Integer> leaderboard) {
        this.gameState = gameState;
        this.leaderboard = leaderboard;
    }

    /**
     * Gets gameState.
     * @return gameState.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Sets gameState.
     * @param gameState The last update of the game state of the finished game.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Gets leaderboard.
     * @return leaderboard.
     */
    public ArrayList<Integer> getLeaderboard() {
        return leaderboard;
    }

    /**
     * Sets leaderboard.
     * @param leaderboard An array list of the players sorted by their amount of money from player with the most
     *                    to the player with the least amount of money.
     */
    public void setLeaderboard(ArrayList<Integer> leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecentGame that = (RecentGame) o;
        return Objects.equals(gameState, that.gameState) && Objects.equals(leaderboard, that.leaderboard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameState, leaderboard);
    }
}
