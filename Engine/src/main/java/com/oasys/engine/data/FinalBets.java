package com.oasys.engine.data;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a stack of the final bets on the winning camel and stack of the final bets on the losing camel.
 */
public class FinalBets {
    public ArrayList<FinalBet> firstCamel;
    public ArrayList<FinalBet> lastCamel;

    /**
     * Initializes an object of FinalBets.
     * @param firstCamel An array list of the final bets on the winning camel.
     * @param lastCamel An array list of the bets on the losing camel.
     */
    public FinalBets(ArrayList<FinalBet> firstCamel, ArrayList<FinalBet> lastCamel) {
        this.firstCamel = firstCamel;
        this.lastCamel = lastCamel;
    }

    /**
     * Gets firstCamel.
     * @return firstCamel.
     */
    public ArrayList<FinalBet> getFirstCamel() {
        return firstCamel;
    }

    /**
     * Sets firstCamel.
     * @param firstCamel An array list of the final bets on the winning camel.
     */
    public void setFirstCamel(ArrayList<FinalBet> firstCamel) {
        this.firstCamel = firstCamel;
    }

    /**
     * Gets lastCamel.
     * @return lastCamel.
     */
    public ArrayList<FinalBet> getLastCamel() {
        return lastCamel;
    }

    /**
     * Sets lastCamel.
     * @param lastCamel An array list of the bets on the losing camel.
     */
    public void setLastCamel(ArrayList<FinalBet> lastCamel) {
        this.lastCamel = lastCamel;
    }

    /**
     * Checks equality of another object with the object of FinalBets..
     * @param o Another object.
     * @return True iff firstCamel and lastCamel are identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalBets finalBets = (FinalBets) o;
        return Objects.equals(firstCamel, finalBets.firstCamel) && Objects.equals(lastCamel, finalBets.lastCamel);
    }

    /**
     * Hashes the object of FinalBets by using firstCamel and lastCamel.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstCamel, lastCamel);
    }
}
