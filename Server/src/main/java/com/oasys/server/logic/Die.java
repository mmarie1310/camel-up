package com.oasys.server.logic;

/**
 * Represents a dice (Die) used in the game.
 * Each dice has an ID and can generate a random value when rolled.
 */
public class Die {
    private final int id; // Unique ID of the die
    private int value;    // Current value of the die

    /**
     * Constructor to initialize a die with a unique ID.
     *
     * @param id Unique ID for the die.
     */
    public Die(int id) {
        this.id = id;
        this.value = 0; // Default value
    }

    /**
     * Rolls the die to generate a random value between 1 and maxValue (inclusive).
     *
     * @param maxValue Maximum value the die can roll.
     */
    public void roll(int maxValue) {
        this.value = (int) (Math.random() * maxValue) + 1;
    }

    /**
     * Resets the die's value to zero.
     */
    public void reset() {
        this.value = 0;
    }

    /**
     * Gets the current value of the die.
     *
     * @return Current value of the die.
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the unique ID of the die.
     *
     * @return ID of the die.
     */
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Die{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
