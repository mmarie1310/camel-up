package com.oasys.server.data;

import java.util.Objects;

/**
 * This data class represents the bettingcards, that every player can place in the beginning of his turn.
 * It describes the camel with its ID the player bets on and the worth of the card.
 */
public class BettingCard {
    private int worth;
    private int camelId;

    /**
     * Initializes an object of class BetCard.
     * @param worth of betcard. Can only be in range: 2-32766.
     * @param camelId of camel the player bets on. Can only be in range: -2 - 32767.
     */
    public BettingCard(int camelId, int worth) throws IllegalArgumentException {
        if (worth < 2 || worth > 32766) {
            throw new IllegalArgumentException("worth of the betcard must be in the following value range: 2-32766.");
        } else if (camelId < -2 || camelId > 32767) {
            throw new IllegalArgumentException("the camel id must be in the following range: -2 - 32767");
        }
        this.worth = worth;
        this.camelId = camelId;
    }

    /**
     * Converts a String to a BettingCard object.
     * The input format must be: "CamelID - Worth".
     * @param input String representing a betting card.
     * @return A new BettingCard object.
     * @throws IllegalArgumentException if the input format is invalid.
     */
    public static BettingCard fromString(String input) {
        String[] parts = input.split(" - ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid input format. Expected: 'CamelID - Worth'.");
        }
        try {
            int camelId = Integer.parseInt(parts[0].replace("Camel ", "").trim());
            int worth = Integer.parseInt(parts[1].replace("Worth ", "").trim());
            return new BettingCard(camelId, worth);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric values in input: " + input, e);
        }
    }

    /**
     * Gets worth.
     * @return worth.
     */
    public int getWorth() {
        return this.worth;
    }

    /**
     * Sets worth.
     * @param worth Only in range 2-32766.
     */
    public void setWorth(int worth) throws IllegalArgumentException {
        if (worth < 2 || worth > 32766) {
            throw new IllegalArgumentException("worth of the betcard must be in the following value range: 2-32766.");
        }
        this.worth = worth;
    }

    /**
     * Gets camelId.
     * @return camelId.
     */
    public int getCamelId() {
        return camelId;
    }

    /**
     * Sets camelId.
     * @param camelId of the camel the player bets on.
     */
    public void setCamelId(int camelId) {
        if (camelId < -2 || camelId > 32767) {
            throw new IllegalArgumentException("the camel id must be in the following range: -2 - 32767");
        }
        this.camelId = camelId;
    }

    /**
     * Checks equality of another object with a betting card.
     * @param o Another object.
     * @return true iff ID and worth are identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BettingCard that = (BettingCard) o;
        return worth == that.worth && camelId == that.camelId;
    }

    /**
     * Hashes the betting card using the camel Id.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(camelId);
    }
}
