package com.oasys.observer.logic;

import com.oasys.observer.data.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the data of a player in the game, including attributes like name, coins, bets, and rank.
 */
public class PlayerData implements EventListener {

    private MethodHandler methodHandler;

    // Static counter to assign unique IDs to each player
    private static int idCounter = 0;

    // Unique identifier for the player
    private final int playerId;

    // Unique identifier for the player from the server
    private final int playerIdFromServer;

    // Name of the player
    private String playerName;

    // Number of coins the player has
    private int coins;

    // Count of bets where the player bet on a winner
    private int winnerBetCount;

    // Count of bets where the player bet on a loser
    private int loserBetCount;

    // Player's rank in the game
    private int rank;

    private int turnOrder;

    // Indicates whether the player has placed a tile
    private boolean hasPlacedTile;

    // List of bets the player has placed during different stages
    private final List<StageBet> stageBets = new ArrayList<>();

    /**
     * Constructs a new PlayerData instance with the specified attributes.
     *
     * @param playerName     The name of the player.
     * @param coins          The number of coins the player has.
     * @param winnerBetCount The number of bets on winners.
     * @param loserBetCount  The number of bets on losers.
     * @param turnOrder      When the player will have their turn. 1 if it's their turn now.
     * @param hasPlacedTile  Whether the player has placed a tile.
     */
    public PlayerData(int playerIdFromServer, String playerName, int coins, int winnerBetCount, int loserBetCount, int turnOrder, boolean hasPlacedTile, MethodHandler methodHandler) {
        this.playerId = ++idCounter;
        this.playerIdFromServer = playerIdFromServer;
        this.playerName = playerName;
        this.coins = coins;
        this.winnerBetCount = winnerBetCount;
        this.loserBetCount = loserBetCount;
        this.turnOrder = turnOrder;
        this.hasPlacedTile = hasPlacedTile;
        this.methodHandler = methodHandler;
        methodHandler.getBoardUpdater().subscribe(BoardEvent.PLAYERS_UPDATED, this);
    }

    /**
     * Gets the unique player ID.
     *
     * @return The player ID.
     */
    public int getPlayerID() {
        return playerId;
    }

    /**
     * Gets the player's name.
     *
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the player's name.
     *
     * @param playerName The new player name.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the number of coins the player has.
     *
     * @return The coin count.
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Sets the number of coins the player has.
     *
     * @param coins The new coin count.
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * Gets the count of winner bets.
     *
     * @return The number of winner bets.
     */
    public int getWinnerBetCount() {
        return winnerBetCount;
    }

    /**
     * Sets the count of winner bets.
     *
     * @param winnerBetCount The new winner bet count.
     */
    public void setWinnerBetCount(int winnerBetCount) {
        this.winnerBetCount = winnerBetCount;
    }

    /**
     * Gets the count of loser bets.
     *
     * @return The number of loser bets.
     */
    public int getLoserBetCount() {
        return loserBetCount;
    }

    /**
     * Sets the count of loser bets.
     *
     * @param loserBetCount The new loser bet count.
     */
    public void setLoserBetCount(int loserBetCount) {
        this.loserBetCount = loserBetCount;
    }

    /**
     * Gets the player's rank.
     *
     * @return The player's rank.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets the player's rank.
     *
     * @param rank The new rank.
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Checks if the player has placed a tile.
     *
     * @return {@code true} if the player has placed a tile; otherwise, {@code false}.
     */
    public boolean isHasPlacedTile() {
        return hasPlacedTile;
    }

    /**
     * Sets whether the player has placed a tile.
     *
     * @param hasPlacedTile {@code true} if the player has placed a tile; otherwise, {@code false}.
     */
    public void setHasPlacedTile(boolean hasPlacedTile) {
        this.hasPlacedTile = hasPlacedTile;
    }

    /**
     * Gets the list of stage bets made by the player.
     *
     * @return The list of stage bets.
     */
    public List<StageBet> getStageBets() {
        return stageBets;
    }

    /**
     * Gets the player ID assigned from the server.
     */
    public int getPlayerIdFromServer() {
        return playerIdFromServer;
    }

    /**
     * Gets the turn order of the player.
     * @return
     */
    public int getTurnOrder() {
        return turnOrder;
    }

    /**
     * Sets the turn order of the player.
     * @param turnOrder
     */
    public void setTurnOrder(int turnOrder) {
        this.turnOrder = turnOrder;
    }

    @Override
    public void update(BoardEvent event) {
        switch (event) {
            case PLAYERS_UPDATED:

        }
    }
}

