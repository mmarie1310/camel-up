package com.oasys.server.data;


import java.util.List;
import java.util.Objects;
import com.oasys.server.data.RolledDice;


/**
 * Represents a camel of a game.
 */
public class Camel {
    private int id;
    private String color;
    private List<BettingCard> bettingCards;
    private RolledDice dice;

    /**
     * Initializes a camel.
     * @param id The ID of the camel. Backwards running camels have the IDs -2 and -1, whereas forwards running camels
     *           have a unique non-negative ID up to 32767.
     * @param color The color of the camel in the form of a hexadecimal color code, e.g., "#FFCCEE".
     */
    public Camel(int id, String color) {
        if (id < -2 || id > 32767) {
            throw new IllegalArgumentException("the camel id must be in the following range: -2 - 32767");
        }
        this.id = id;
        this.color = color;
    }

    /**
     * Whether the camel goes forwards or backwards.
     * @return Whether the camel goes forwards or backwards.
     */
    public boolean goesForward() {
        return this.id >= 0;
    }

    /**
     * Gets the ID of the camel.
     * @return The ID of the camel.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the ID of the camel.
     * @param id The ID of the camel.  Backwards running camels have the IDs -2 and -1, whereas forwards running camels
     *           have a unique non-negative ID up to 32767.
     */
    public void setId(int id) {
        if (id < -2 || id > 32767) {
            throw new IllegalArgumentException("the camel id must be in the following range: -2 - 32767");
        }
        this.id = id;
    }

    /**
     * Gets the color of the camel.
     * @return The color of the camel in the form of a hexadecimal color code, e.g., "#FFCCEE".
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Sets the color of the camel.
     * @param color The color of the camel in the form of a hexadecimal color code, e.g., "#FFCCEE".
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Checks if a camel has a tent.
     * This method could be extended if the data structure for tents exists.
     * @return true if the camel has a tent.
     */
    public boolean hasTent() {
        return false;
    }

    public List<BettingCard> getBettingCards() {
        return this.bettingCards;
    }

    public boolean hasValidDice() {
        return this.dice != null;
    }

    public RolledDice getDice() {
        return this.dice;
    }



    /**
     * Checks equality of another object with a camel.
     * @param o Another object.
     * @return true iff ID and color are identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Camel camel = (Camel) o;
        return this.id == camel.id && Objects.equals(this.color, camel.color);
    }

    /**
     * Hashes the camel by using its ID and color.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.color);
    }
}
