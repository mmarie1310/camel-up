package com.oasys.server.logic;

import java.util.Stack;
import com.oasys.server.data.BettingCard;

/**
 * Represents a betting stack for winner and loser bets in the game.
 * Each stack holds betting cards, which are added and removed during gameplay.
 */
public class BettingStack {
    private final Stack<BettingCard> stack;

    /**
     * Initializes an empty betting stack.
     */
    public BettingStack() {
        this.stack = new Stack<>();
    }

    /**
     * Adds a betting card to the stack.
     *
     * @param card The betting card to add.
     */
    public void addCard(BettingCard card) {
        stack.push(card);
    }

    /**
     * Removes and returns the top betting card from the stack.
     *
     * @return The betting card at the top of the stack, or null if the stack is empty.
     */
    public BettingCard removeCard() {
        return stack.isEmpty() ? null : stack.pop();
    }

    /**
     * Checks if the stack is empty.
     *
     * @return True if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Gets the size of the stack.
     *
     * @return The number of betting cards in the stack.
     */
    public int size() {
        return stack.size();
    }

    /**
     * Clears all cards from the stack.
     */
    public void clear() {
        stack.clear();
    }

    /**
     * Adds a betting card using a generic add method.
     *
     * @param card The betting card to add.
     */
    public void add(BettingCard card) {
        addCard(card);
    }

    @Override
    public String toString() {
        return "BettingStack{" +
                "stack=" + stack +
                '}';
    }
}
