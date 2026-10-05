package com.oasys.server.logic;

import com.oasys.server.data.Camel;
import java.util.Stack;

/**
 * Represents a herd of camels on a field.
 * Provides functionality to manage and manipulate camel stacks.
 */
public class CamelHerd {
    private Stack<Camel> camels = new Stack<>();

    /**
     * Constructs a new CamelHerd with an initial camel.
     *
     * @param initialCamel The first camel in the herd.
     */
    public CamelHerd(Camel initialCamel) {
        if (initialCamel != null) {
            camels.push(initialCamel);
        }
    }

    /**
     * Retrieves the stack of camels in the herd.
     *
     * @return The stack of camels.
     */
    public Stack<Camel> getCamels() {
        return camels;
    }

    /**
     * Retrieves the top camel in the herd.
     *
     * @return The top camel.
     */
    public Camel getTopCamel() {
        return camels.isEmpty() ? null : camels.peek();
    }

    /**
     * Retrieves the bottom camel in the herd.
     *
     * @return The bottom camel.
     */
    public Camel getBottomCamel() {
        return camels.isEmpty() ? null : camels.firstElement();
    }

    /**
     * Checks if the herd is empty.
     *
     * @return True if the herd is empty, false otherwise.
     */
    public boolean isEmpty() {
        return camels.isEmpty();
    }

    /**
     * Adds all camels from another herd to this herd.
     *
     * @param otherHerd The herd to merge into this herd.
     */
    public void addCamels(CamelHerd otherHerd) {
        if (otherHerd == null || otherHerd.isEmpty()) {
            return;
        }
        Stack<Camel> reverseStack = new Stack<>();
        while (!otherHerd.camels.isEmpty()) {
            reverseStack.push(otherHerd.camels.pop());
        }
        while (!reverseStack.isEmpty()) {
            camels.push(reverseStack.pop());
        }
    }

    /**
     * Splits the herd above the specified camel, creating a new herd.
     *
     * @param camel The camel to split the herd above.
     * @return A new CamelHerd containing the split camels.
     * @throws IllegalArgumentException If the camel is not in the herd.
     */
    public CamelHerd splitAbove(Camel camel) {
        if (!camels.contains(camel)) {
            throw new IllegalArgumentException("Camel not found in the herd.");
        }

        Stack<Camel> above = new Stack<>();
        while (!camels.isEmpty() && camels.peek() != camel) {
            above.push(camels.pop());
        }

        above.push(camels.pop()); // Include the specified camel

        CamelHerd newHerd = new CamelHerd(null);
        while (!above.isEmpty()) {
            newHerd.camels.push(above.pop());
        }

        return newHerd;
    }

    /**
     * Moves all camels in this herd to another herd.
     *
     * @param destination The destination herd.
     */
    public void moveTo(CamelHerd destination) {
        if (destination == null) {
            return;
        }
        while (!camels.isEmpty()) {
            destination.camels.push(camels.pop());
        }
    }

    /**
     * Returns a string representation of the camel herd.
     *
     * @return String representation of the camel herd.
     */
    @Override
    public String toString() {
        return "CamelHerd{" +
                "camels=" + camels +
                '}';
    }
}
