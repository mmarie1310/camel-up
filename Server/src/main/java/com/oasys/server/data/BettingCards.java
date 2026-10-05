package com.oasys.server.data;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a stack of betting cards, specifically a stack of objects of class BettingCard.
 */
public class BettingCards {
    public int camelId;
    public int amount;

    /**
     * Initializes an object of BettingCards.
     * @param camelId The ID of the camel that is bet on.
     * @param amount The amount of betting cards of this stack.
     */
    public BettingCards(int camelId, int amount) {
        if (camelId < -2 || camelId > 32767) {
            throw new IllegalArgumentException("the camel id must be in the following range: -2 - 32767");
        } else if (amount < 0) {
            throw new IllegalArgumentException("the amount must be positive or zero");
        }
        this.camelId = camelId;
        this.amount = amount;
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
     * @param camelId The ID of the camel that is bet on.
     */
    public void setCamelId(int camelId) {
        this.camelId = camelId;
    }

    /**
     * Gets amount.
     * @return amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets amount.
     * @param amount The amount of betting cards of this stack.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Checks equality of another object with a betting card stack.
     * @param o Another object.
     * @return true iff ID and amount are identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BettingCards that = (BettingCards) o;
        return camelId == that.camelId && amount == that.amount;
    }

    //generates and sorts the betting card
    public void generateCards(int count) {
        ArrayList<BettingCard> cards = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            int value = (i == 1) ? 2 : (i == count) ? count + 1 : i;
            cards.add(new BettingCard(this.camelId, value));
        }
        this.amount = cards.size();
    }

    /**
     * Hashes the betting card stack by using the camel's ID.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(camelId);
    }


}
