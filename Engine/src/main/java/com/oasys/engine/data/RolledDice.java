package com.oasys.engine.data;
import java.util.Objects;

/**
 * This class simulates rolling a dice in CamelUp.
 */
public class RolledDice {
    private int camelId;
    private int number;

    /**
     * Initializes an object of RolledDice.
     * @param camelId The ID of the camel of the dice.
     * @param number The number of positions the camel is moved.
     */
    public RolledDice(int camelId, int number) {
        this.camelId = camelId;
        this.number = number;
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
     * @param camelId The ID of the camel of the dice.
     */
    public void setCamelId(int camelId) {
        this.camelId = camelId;
    }

    /**
     * Gets number.
     * @return number.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets number.
     * @param number The number of positions the camel is moved.
     */
    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolledDice that = (RolledDice) o;
        return camelId == that.camelId && number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(camelId);
    }
}






