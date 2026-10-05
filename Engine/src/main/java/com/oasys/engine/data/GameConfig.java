package com.oasys.engine.data;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The configuration of a game.
 */
public class GameConfig {
    private int playerCount;
    private int numberOfSpaces;
    private ArrayList<Camel> camels;
    private int diceFaces;
    private int numberOfBettingCards;
    public int thinkingTime;
    public int visualizationTime;
    public IllegalMovePenalty illegalMovePenalty;
    public int maxGameDuration;
    public int maxTurns;

    /**
     * Initializes the game configuration.
     * @param playerCount The maximum number of players in a game. (value range: 2-6)
     * @param numberOfSpaces The number of fields on the board. (value range: 3-32766)
     * @param camels The camels in the game.
     * @param diceFaces The number of faces of the dices. (value range: 1-10922)
     * @param numberOfBettingCards The number of betting cards for each camel. (value range: 3-32767)
     * @param thinkingTime The time a player can take to make a move (in ms). (must be positive)
     * @param visualizationTime The maximum allowed time for the visualization of a move (in ms). (must be positive)
     * @param illegalMovePenalty The penalty for an illegal move or when no move is made at all.
     * @param maxGameDuration The maximum duration of the entire game (in ms). (must be positive)
     * @param maxTurns The maximum number of rounds in a game. (value range: 1-32767)
     * @throws IllegalArgumentException If any argument is not in the corresponding value range.
     */
public GameConfig(int playerCount, int numberOfSpaces, ArrayList<Camel> camels, int diceFaces, int numberOfBettingCards,
                  int thinkingTime, int visualizationTime, IllegalMovePenalty illegalMovePenalty,
                  int maxGameDuration, int maxTurns) throws IllegalArgumentException {
        if (playerCount < 2 || playerCount > 6) {
            throw new IllegalArgumentException("playerCount must be in the following value range: 2-6");
        } else if (numberOfSpaces < 3 || numberOfSpaces > 32766) {
            throw new IllegalArgumentException("numberOfSpaces must be in the following value range: 3-32766");
        } else if (diceFaces < 1 || diceFaces > 10922) {
            throw new IllegalArgumentException("diceFaces must be in the following value range: 1-10922");
        } else if (numberOfBettingCards < 3 || numberOfBettingCards > 32767) {
            throw new IllegalArgumentException("numberOfBettingCards must be in the following value range: 3-32767");
        } else if (thinkingTime < 1) {
            throw new IllegalArgumentException("thinkingTime must be positive");
        } else if (visualizationTime < 1) {
            throw new IllegalArgumentException("visualizationTime must be positive");
        } else if (maxGameDuration < 1) {
            throw new IllegalArgumentException("maxGameDuration must be positive");
        } else if (maxTurns < 1 || maxTurns > 32767) {
            throw new IllegalArgumentException("maxTurns must be in the following value range: 1-32767");
        }

        this.playerCount = playerCount;
        this.numberOfSpaces = numberOfSpaces;
        this.camels = camels;
        this.diceFaces = diceFaces;
        this.numberOfBettingCards = numberOfBettingCards;
        this.thinkingTime = thinkingTime;
        this.visualizationTime = visualizationTime;
        this.illegalMovePenalty = illegalMovePenalty;
        this.maxGameDuration = maxGameDuration;
        this.maxTurns = maxTurns;
    }

    /**
     * Gets the maximum number of players in a game.
     * @return The maximum number of players in a game.
     */
    public int getPlayerCount() {
        return this.playerCount;
    }

    /**
     * Sets the maximum number of players in a game.
     * @param playerCount The maximum number of players in a game. (value range: 2-6)
     * @throws IllegalArgumentException If the new value is not in the given range.
     */
    public void setPlayerCount(int playerCount) throws IllegalArgumentException {
        if (playerCount < 2 || playerCount > 6) {
            throw new IllegalArgumentException("playerCount must be in the following value range: 2-6");
        }
        this.playerCount = playerCount;
    }

    /**
     * Gets the number of fields on the board.
     * @return The number of fields on the board.
     */
    public int getNumberOfSpaces() {
        return this.numberOfSpaces;
    }

    /**
     * Sets number of fields on the board.
     * @param numberOfSpaces The number of fields on the board. (value range: 3-32766)
     * @throws IllegalArgumentException If the new value is not in the given range.
     */
    public void setNumberOfSpaces(int numberOfSpaces) throws IllegalArgumentException {
        if (numberOfSpaces < 3 || numberOfSpaces > 32766) {
            throw new IllegalArgumentException("numberOfSpaces must be in the following value range: 3-32766");
        }
        this.numberOfSpaces = numberOfSpaces;
    }

    /**
     * Gets the camels in the game.
     * @return The camels in the game.
     */
    public ArrayList<Camel> getCamels() {
        return this.camels;
    }

    /**
     * Sets the camels in the game.
     * @param camels The camels in the game.
     */
    public void setCamels(ArrayList<Camel> camels) {
        this.camels = camels;
    }

    /**
     * Gets the number of faces of the dices.
     * @return The number of faces of the dices.
     */
    public int getDiceFaces() {
        return this.diceFaces;
    }

    /**
     * Sets the number of faces of the dices.
     * @param diceFaces The number of faces of the dices. (value range: 1-10922)
     * @throws IllegalArgumentException If the new value is not in the given range.
     */
    public void setDiceFaces(int diceFaces) throws IllegalArgumentException {
        if (diceFaces < 1 || diceFaces > 10922) {
            throw new IllegalArgumentException("diceFaces must be in the following value range: 1-10922");
        }
        this.diceFaces = diceFaces;
    }

    /**
     * Gets the number of betting cards for each camel.
     * @return The number of betting cards for each camel.
     */
    public int getNumberOfBettingCards() {
        return this.numberOfBettingCards;
    }

    /**
     * Sets the number of betting cards for each camel.
     * @param numberOfBettingCards The number of betting cards for each camel. (value range: 3-32767)
     * @throws IllegalArgumentException If the new value is not in the given range.
     */
    public void setNumberOfBettingCards(int numberOfBettingCards) throws IllegalArgumentException {
        if (numberOfBettingCards < 3 || numberOfBettingCards > 32767) {
            throw new IllegalArgumentException("numberOfBettingCards must be in the following value range: 3-32767");
        }
        this.numberOfBettingCards = numberOfBettingCards;
    }

    /**
     * Gets the time a player can take to make a move.
     * @return The time a player can take to make a move (in ms).
     */
    public int getThinkingTime() {
        return this.thinkingTime;
    }

    /**
     * Sets the time a player can take to make a move.
     * @param thinkingTime The time a player can take to make a move (in ms). (must be positive)
     * @throws IllegalArgumentException If the value is not positive.
     */
    public void setThinkingTime(int thinkingTime) throws IllegalArgumentException {
        if (thinkingTime < 1) {
            throw new IllegalArgumentException("thinkingTime must be positive");
        }
        this.thinkingTime = thinkingTime;
    }

    /**
     * Gets the maximum allowed time for the visualization of a move.
     * @return The maximum allowed time for the visualization of a move (in ms).
     */
    public int getVisualizationTime() {
        return this.visualizationTime;
    }

    /**
     * Sets the maximum allowed time for the visualization of a move.
     * @param visualizationTime The maximum allowed time for the visualization of a move (in ms). (must be positive)
     * @throws IllegalArgumentException If the value is not positive.
     */
    public void setVisualizationTime(int visualizationTime) throws IllegalArgumentException {
        if (visualizationTime < 1) {
            throw new IllegalArgumentException("visualizationTime must be positive");
        }
        this.visualizationTime = visualizationTime;
    }

    /**
     * Gets the penalty for an illegal move or when no move is made at all.
     * @return The penalty for an illegal move or when no move is made at all.
     */
    public IllegalMovePenalty getIllegalMovePenalty() {
        return this.illegalMovePenalty;
    }

    /**
     * Sets the penalty for an illegal move or when no move is made at all.
     * @param illegalMovePenalty The penalty for an illegal move or when no move is made at all.
     */
    public void setIllegalMovePenalty(IllegalMovePenalty illegalMovePenalty) {
        this.illegalMovePenalty = illegalMovePenalty;
    }

    /**
     * Gets the maximum duration of the entire game.
     * @return The maximum duration of the entire game (in ms).
     */
    public int getMaxGameDuration() {
        return this.maxGameDuration;
    }

    /**
     * Sets the maximum duration of the entire game.
     * @param maxGameDuration The maximum duration of the entire game (in ms). (must be positive)
     * @throws IllegalArgumentException If the value is not positive.
     */
    public void setMaxGameDuration(int maxGameDuration) throws IllegalArgumentException {
        if (maxGameDuration < 1) {
            throw new IllegalArgumentException("maxGameDuration must be positive");
        }
        this.maxGameDuration = maxGameDuration;
    }

    /**
     * Gets the maximum number of turns in a game.
     * @return The maximum number of turns in a game.
     */
    public int getMaxTurns() {
        return this.maxTurns;
    }

    /**
     * Sets the maximum number of turns in a game.
     * @param maxTurns The maximum number of turns in a game. (value range: 1-32767)
     * @throws IllegalArgumentException If the value is not in the given range.
     */
    public void setMaxRounds(int maxTurns) throws IllegalArgumentException {
        if (maxTurns < 1 || maxTurns > 32767) {
            throw new IllegalArgumentException("maxTurns must be in the following value range: 1-32767");
        }
        this.maxTurns = maxTurns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameConfig that = (GameConfig) o;
        return playerCount == that.playerCount && numberOfSpaces == that.numberOfSpaces && diceFaces == that.diceFaces && numberOfBettingCards == that.numberOfBettingCards && thinkingTime == that.thinkingTime && visualizationTime == that.visualizationTime && maxGameDuration == that.maxGameDuration && maxTurns == that.maxTurns && Objects.equals(camels, that.camels) && illegalMovePenalty == that.illegalMovePenalty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerCount, numberOfSpaces, camels, diceFaces, numberOfBettingCards, thinkingTime, visualizationTime, illegalMovePenalty, maxGameDuration, maxTurns);
    }
}
