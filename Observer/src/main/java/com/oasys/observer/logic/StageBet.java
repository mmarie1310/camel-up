package com.oasys.observer.logic;

/**
 * Represents a bet placed on a specific camel during a stage of the game.
 */
public class StageBet {
    // The camel this bet is placed on
    private final String camel;

    // The value of the bet
    private final int value;

    /**
     * Constructs a new StageBet with the specified camel and bet value.
     *
     * @param camel The camel this bet is associated with.
     * @param value The value of the bet.
     */
    public StageBet(String camel, int value) {
        this.camel = camel;
        this.value = value;
    }

    /**
     * Gets the camel this bet is associated with.
     *
     * @return The camel's name.
     */
    public String getCamel() {
        return camel;
    }

    /**
     * Gets the value of the bet.
     *
     * @return The bet value.
     */
    public int getValue() {
        return value;
    }

}