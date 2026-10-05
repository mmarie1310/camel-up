package com.oasys.server.logic;

import com.oasys.server.data.Camel;
import com.oasys.server.data.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Random;


/**
 * Represents the game board for the Camel racing game.
 */
public class GameBoard {
    private final List<Field> fields = new ArrayList<>();
    private final List<Camel> camels = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private final BettingStack winnerStack = new BettingStack();
    private final BettingStack loserStack = new BettingStack();
    private Die sharedBackwardDie; // Shared die for backward-moving camels
    private Field finishLineField;
    private List<Die> diceList = new ArrayList<>();

    /**
     * Constructs a new game board.
     *
     * @param numberOfFields The number of fields on the board.
     * @param numberOfForwardCamels The number of forward-moving camels.
     * @param bettingCardCount The number of betting cards per camel.
     */
    public GameBoard(int numberOfFields, int numberOfForwardCamels, int bettingCardCount) {
        for (int i = 0; i < numberOfFields; i++) {
            fields.add(new Field(i));
        }
        buildConnections();
        assignSpecialFields();
        createCamels(numberOfForwardCamels, bettingCardCount);
        initializeDice(numberOfForwardCamels);
    }

    private void buildConnections() {
        int numberOfFields = fields.size();
        for (int i = 0; i < numberOfFields; i++) {
            Field current = fields.get(i);
            Field next = fields.get((i + 1) % numberOfFields);
            Field previous = fields.get((i - 1 + numberOfFields) % numberOfFields);

            current.addConnection(next);
            current.addConnection(previous);
        }
    }

    private void assignSpecialFields() {
        if (fields.size() < 3) {
            throw new IllegalArgumentException("The board must have at least 3 fields.");
        }
        Field startField = fields.get(0);
        startField.setStartField(true);
        finishLineField = startField;
        finishLineField.setFinishLine(true);
        Field endField = fields.get(1);
        endField.setEndField(true);
    }

    private void createCamels(int numberOfForwardCamels, int bettingCardCount) {
        for (int i = 0; i < numberOfForwardCamels; i++) {
            Camel camel = new Camel(i, true);
            camel.setCurrentField(fields.get(0));
            camel.setTent(fields.get(i % fields.size()));
            camel.setBettingCards(bettingCardCount);
            camels.add(camel);
            fields.get(0).addCamel(camel);
        }

        for (int i = 0; i < 2; i++) {
            Camel camel = new Camel(numberOfForwardCamels + i, false);
            camel.setCurrentField(fields.get(fields.size() - 1));
            camels.add(camel);
            fields.get(fields.size() - 1).addCamel(camel);
        }

        sharedBackwardDie = new Die(-1);
    }

    private void initializeDice(int numberOfForwardCamels) {
        for (int i = 0; i < numberOfForwardCamels; i++) {
            diceList.add(new Die(i));
        }
    }

    public void determineStartPositions() {
        Random random = new Random();
        for (Camel camel : camels) {
            int roll = random.nextInt(fields.size() / 3) + 1;
            Field startField = camel.isForward()
                    ? fields.get((fields.indexOf(finishLineField) + roll) % fields.size())
                    : fields.get((fields.indexOf(finishLineField) - roll + fields.size()) % fields.size());
            camel.setCurrentField(startField);
            startField.addCamel(camel);
        }
    }

    public void rollBackwardDie() {
        Random random = new Random();
        int selectedCamelIndex = random.nextInt(2);
        Camel selectedCamel = camels.stream()
                .filter(camel -> !camel.isForward())
                .toList()
                .get(selectedCamelIndex);

        int roll = random.nextInt(fields.size() / 3) + 1;
        Field currentField = selectedCamel.getCurrentField();
        Field nextField = fields.get((fields.indexOf(currentField) - roll + fields.size()) % fields.size());

        Stack<Camel> herd = currentField.splitHerdAbove(selectedCamel);
        nextField.combineHerd(herd);
        selectedCamel.setCurrentField(nextField);
    }

    public void resetResources() {
        for (Camel camel : camels) {
            camel.setCurrentField(fields.get(0));
            fields.get(0).addCamel(camel);
        }
        winnerStack.clear();
        loserStack.clear();
    }

    public void generateTargetCards() {
        for (Player player : players) {
            for (Camel camel : camels) {
                player.addTargetCard("TargetCard-Camel-" + camel.getId());
            }
        }
    }

    /**
     * Simulates the movement of a camel on the game board.
     *
     * @param camelId The ID of the camel to move.
     * @param steps The number of steps the camel should move.
     */
    public void simulateCamelMovement(int camelId, int steps) {
        Camel camel = camels.stream()
                .filter(c -> c.getId() == camelId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Camel with ID " + camelId + " not found"));

        // Get the current position of the camel
        Field currentField = camel.getCurrentField();

        // Calculate the new position
        int currentIndex = fields.indexOf(currentField);
        int newIndex;
        if (camel.isForward()) {
            newIndex = (currentIndex + steps) % fields.size(); // Forward movement
        } else {
            newIndex = (currentIndex - steps + fields.size()) % fields.size(); // Backward movement
        }

        // Update the camel's position
        Field newField = fields.get(newIndex);
        Stack<Camel> herd = currentField.splitHerdAbove(camel);

        if (newField.getCamelHerd() != null) {
            newField.combineHerd(herd);
        } else {
            newField.setCamelHerd(herd);
        }

        camel.setCurrentField(newField);
        System.out.println("Camel " + camelId + " moved to field " + newField.getId());
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Camel> getCamels() {
        return camels;
    }

    public List<Die> getDice() {
        return diceList;
    }

    /**
     * Gets the stack of winner bets.
     *
     * @return The BettingStack for the winner bets.
     */
    public BettingStack getWinnerStack() {
        return winnerStack;
    }

    /**
     * Gets the stack of loser bets.
     *
     * @return The BettingStack for the loser bets.
     */
    public BettingStack getLoserStack() {
        return loserStack;
    }

    /**
     * Places a spectator tile on the specified field.
     *
     * @param fieldId The ID of the field where the tile should be placed.
     * @return True if the tile was successfully placed, false otherwise.
     */
    public boolean placeSpectatorTile(int fieldId) {
        Field field = fields.get(fieldId);
        Field leftNeighbor = fieldId > 0 ? fields.get(fieldId - 1) : null;
        Field rightNeighbor = fieldId < fields.size() - 1 ? fields.get(fieldId + 1) : null;

        if (field.canPlaceSpectatorTile(leftNeighbor, rightNeighbor)) {
            field.setSpectatorTile(true);
            System.out.println("Spectator tile placed on field " + fieldId);
            return true;
        } else {
            System.out.println("Cannot place spectator tile on field " + fieldId);
            return false;
        }
    }
}
