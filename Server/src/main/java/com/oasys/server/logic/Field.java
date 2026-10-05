package com.oasys.server.logic;

import com.oasys.server.data.Camel;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents a single field on the game board.
 * Each field has a unique ID, connections to exactly two other fields,
 * and attributes to indicate if it is a special field (start, end, or finish line).
 */

public class Field {
    private final int id; // Unique identifier for the field.
    private final List<Field> connections = new ArrayList<>(); // List of connected fields (exactly 2 connections).
    private boolean isStartField = false; // Indicates if this field is the start field.
    private boolean isEndField = false; // Indicates if this field is the end field.
    private boolean isFinishLine = false; // Indicates if this field contains the finish line.
    private boolean hasSpectatorTile = false; // Indicates if a spectator tile is placed on this field.
    private Stack<Camel> camelHerd = new Stack<>(); // Represents the stack of camels on this field.

    /**
     * Constructor for creating a field with a unique ID.
     *
     * @param id Unique identifier for the field.
     */
    public Field(int id) {
        this.id = id;
    }

    /**
     * Gets the unique identifier of the field.
     *
     * @return The field's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the list of fields connected to this field.
     *
     * @return List of connected fields.
     */
    public List<Field> getConnections() {
        return connections;
    }

    /**
     * Checks if this field is the start field.
     *
     * @return True if this field is the start field, false otherwise.
     */
    public boolean isStartField() {
        return isStartField;
    }

    /**
     * Sets this field as the start field.
     *
     * @param isStartField True if this field should be the start field.
     */
    public void setStartField(boolean isStartField) {
        this.isStartField = isStartField;
    }

    /**
     * Checks if this field is the end field.
     *
     * @return True if this field is the end field, false otherwise.
     */
    public boolean isEndField() {
        return isEndField;
    }

    /**
     * Sets this field as the end field.
     *
     * @param isEndField True if this field should be the end field.
     */
    public void setEndField(boolean isEndField) {
        this.isEndField = isEndField;
    }

    /**
     * Checks if this field contains the finish line.
     *
     * @return True if this field has the finish line, false otherwise.
     */
    public boolean isFinishLine() {
        return isFinishLine;
    }

    /**
     * Sets this field to contain the finish line.
     *
     * @param isFinishLine True if this field should have the finish line.
     */
    public void setFinishLine(boolean isFinishLine) {
        this.isFinishLine = isFinishLine;
    }

    /**
     * Checks if this field has a spectator tile.
     *
     * @return True if this field has a spectator tile, false otherwise.
     */
    public boolean hasSpectatorTile() {
        return hasSpectatorTile;
    }

    /**
     * Sets a spectator tile on this field.
     *
     * @param hasSpectatorTile True if a spectator tile is placed, false otherwise.
     */
    public void setSpectatorTile(boolean hasSpectatorTile) {
        this.hasSpectatorTile = hasSpectatorTile;
    }

    /**
     * Adds a connection to another field.
     * Ensures that the field has at most two connections and avoids duplicates.
     *
     * @param field The field to connect to this field.
     */
    public void addConnection(Field field) {
        if (connections.size() < 2 && !connections.contains(field)) {
            connections.add(field);
            field.addConnection(this); // Establishes a bidirectional connection.
        }
    }

    /**
     * Retrieves the next field based on the direction of movement.
     *
     * @param forward True for forward movement, false for backward movement.
     * @return The next field in the specified direction.
     * @throws IllegalStateException If the field does not have exactly 2 connections.
     */
    public Field getNextField(boolean forward) {
        if (connections.size() != 2) {
            throw new IllegalStateException("Each field must be connected to exactly 2 fields.");
        }
        return forward ? connections.get(0) : connections.get(1);
    }

    /**
     * Adds a camel to the herd on this field.
     *
     * @param camel The camel to add.
     */
    public void addCamel(Camel camel) {
        camelHerd.push(camel);
    }

    /**
     * Removes the top camel herd starting from the specified camel.
     *
     * @param camel The camel to split the herd from.
     * @return A new stack of camels representing the split herd.
     */
    public Stack<Camel> splitHerdAbove(Camel camel) {
        Stack<Camel> aboveHerd = new Stack<>();
        while (!camelHerd.isEmpty() && camelHerd.peek() != camel) {
            aboveHerd.push(camelHerd.pop());
        }

        if (!camelHerd.isEmpty() && camelHerd.peek() == camel) {
            aboveHerd.push(camelHerd.pop());
        }

        return aboveHerd;
    }

    /**
     * Combines the current camel herd with another herd.
     *
     * @param incomingHerd The herd to combine with the current herd.
     */
    public void combineHerd(Stack<Camel> incomingHerd) {
        while (!incomingHerd.isEmpty()) {
            camelHerd.push(incomingHerd.pop());
        }
    }

    /**
     * Retrieves the camel herd on this field.
     *
     * @return The stack of camels on this field.
     */
    public Stack<Camel> getCamelHerd() {
        return camelHerd;
    }

    /**
     * Sets the camel herd on this field.
     *
     * @param camelHerd The stack of camels to set.
     */
    public void setCamelHerd(Stack<Camel> camelHerd) {
        this.camelHerd = camelHerd;
    }

    /**
     * Checks if a spectator tile can be placed on this field based on the neighboring fields.
     *
     * @param leftNeighbor The left neighbor field.
     * @param rightNeighbor The right neighbor field.
     * @return True if the spectator tile can be placed, false otherwise.
     */
    public boolean canPlaceSpectatorTile(Field leftNeighbor, Field rightNeighbor) {
        if (this.isStartField()) {
            return false; // Spectator tile cannot be placed on the start field.
        }
        if (this.hasSpectatorTile()) {
            return false; // This field already has a spectator tile.
        }
        if ((leftNeighbor != null && leftNeighbor.hasSpectatorTile()) ||
                (rightNeighbor != null && rightNeighbor.hasSpectatorTile())) {
            return false; // Neighboring fields cannot have spectator tiles.
        }
        return true; // All conditions satisfied.
    }

    /**
     * Returns the string representation of the field.
     * Includes the field's ID, connections, and special field attributes.
     *
     * @return String representation of the field.
     */
    @Override
    public String toString() {
        return "Field{" +
                "id=" + id +
                ", connections=" + connections.stream().map(Field::getId).toList() +
                ", isStartField=" + isStartField +
                ", isEndField=" + isEndField +
                ", isFinishLine=" + isFinishLine +
                ", hasSpectatorTile=" + hasSpectatorTile +
                ", camelHerd=" + camelHerd +
                '}';
    }
}
