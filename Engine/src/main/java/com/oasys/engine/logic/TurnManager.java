package com.oasys.engine.logic;

import com.oasys.engine.communication.Communication;
import com.oasys.engine.communication.packets.*;

import com.oasys.engine.data.BettingCards;
import com.oasys.engine.data.BoardSpace;
import com.oasys.engine.data.Camel;
import com.oasys.engine.data.PlayerCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * This class determines the next turn of the engine participant by choosing from all valid moves.
 */
public class TurnManager {
    private static TurnManager turnManagerInstance;
    private Random random;

    private TurnManager() {
        this.random = new Random();
    }

    public static synchronized TurnManager getInstance() {
        if (TurnManager.turnManagerInstance == null) {
            TurnManager.turnManagerInstance = new TurnManager();
        }
        return TurnManager.turnManagerInstance;
    }

    /**
     * Chooses a random turn of a list of currently possible turns.
     *
     * @param gameState the current gamestate.
     */
    public void makeTurn(GameState gameState) {
        ArrayList<Packet> turnPackets = this.allValidTurnPackets(gameState);
        int randomIndex = this.random.nextInt(turnPackets.size());
        Packet turnPacket = turnPackets.get(randomIndex);
        Communication.getInstance().sendPacket(turnPacket);
    }

    /**
     * Creates a list of all valid turns.
     *
     * @param gameState the current game state.
     * @return a Packet.
     */
    public ArrayList<Packet> allValidTurnPackets(GameState gameState) {
        ArrayList<Packet> turnPackets = new ArrayList<>();

        turnPackets.addAll(this.allValidPlacePlayerCardPackets(gameState));
        turnPackets.addAll(this.allValidRollDicePackets(gameState));
        turnPackets.addAll(this.allValidFinalBetPackets(gameState));
        turnPackets.addAll(this.allStageBetPackets(gameState));

        return turnPackets;
    }

    /**
     * Creates a list of all valid possibilities to send PlacePlayerCard Packet.
     * @param gameState new received gamestate.
     * @return list of Packets PlacePlayerCard.
     */
    public ArrayList<PlacePlayerCard> allValidPlacePlayerCardPackets(GameState gameState) {
        ArrayList<BoardSpace> boardSpaces = gameState.getBoardSpaces();

        // check whether engine participant has a player card left
        int clientId = Communication.getInstance().getClientId();
        for (BoardSpace boardSpace : boardSpaces) {
            PlayerCard playerCard = boardSpace.getPlayerCard();
            if (playerCard != null && playerCard.getPlayerId() == clientId) {
                return new ArrayList<>(); // return empty list (engine's player card already placed)
            }
        }

        // find all valid board spaces
        ArrayList<PlacePlayerCard> placePlayerCardPackets = new ArrayList<>();
        for (int i=0; i < boardSpaces.size(); i++) {
            if (i==0) {continue;} // not allowed on start space

            PlayerCard prev = boardSpaces.get(i-1).getPlayerCard();
            PlayerCard cur = boardSpaces.get(i).getPlayerCard();
            PlayerCard next = i+1 == boardSpaces.size() ? null : boardSpaces.get(i+1).getPlayerCard();

            if (prev == null && cur ==  null && next == null) {
                int spaceId = boardSpaces.get(i).getSpaceId();
                placePlayerCardPackets.add(new PlacePlayerCard(spaceId, 1));
                placePlayerCardPackets.add(new PlacePlayerCard(spaceId, -1));
            }
        }

        return placePlayerCardPackets;
    }

    /**
     * Creates a list of all valid possibilities to send RollDice Packet.
     * @param gameState new received game state.
     * @return list of Packets RollDice.
     */
    public ArrayList<RollDice> allValidRollDicePackets(GameState gameState) {
        ArrayList<RollDice> rollDicePackets = new ArrayList<>();
        if ((gameState.getRolledDice().size()) < (gameState.getGameConfig().getCamels().size())) {
            rollDicePackets.add(new RollDice());
        }
        return rollDicePackets;
    }

    /**
     * Creates a list of all valid possibilities to send FinalBet Packet.
     * @param gameState new received game state.
     * @return list of Packets FinalBet.
     */
    public ArrayList<FinalBet> allValidFinalBetPackets(GameState gameState) {
        int clientId = Communication.getInstance().getClientId();

        HashSet<Integer> placedFinalBets = new HashSet<>(); // camel Ids of placed final bets
        for (com.oasys.engine.data.FinalBet finalBet : gameState.getFinalBets().getFirstCamel()) {
            if (finalBet.getPlayerId() == clientId) {
                placedFinalBets.add(finalBet.getCamelId());
            }
        }
        for (com.oasys.engine.data.FinalBet finalBet : gameState.getFinalBets().getLastCamel()) {
            if (finalBet.getPlayerId() == clientId) {
                placedFinalBets.add(finalBet.getCamelId());
            }
        }

        ArrayList<FinalBet> finalBetPackets = new ArrayList<>();
        for (Camel camel : gameState.getGameConfig().getCamels()) {
            if (camel.goesForward() && !placedFinalBets.contains(camel.getId())) {
                finalBetPackets.add(new FinalBet(true, camel.getId()));
                finalBetPackets.add(new FinalBet(false, camel.getId()));
            }
        }
        return finalBetPackets;
    }

    /**
     * Creates a list of all valid possibilities to send StageBet Packet.
     * @param gameState new received game state.
     * @return list of Packets StageBet.
     */
    public ArrayList<StageBet> allStageBetPackets(GameState gameState) {
        ArrayList<StageBet> stageBetPackets = new ArrayList<>();
        for (BettingCards bettingCards : gameState.getBettingCards()) {
            if (bettingCards.getAmount() > 0) {
                stageBetPackets.add(new StageBet(bettingCards.getCamelId()));
            }
        }
        return stageBetPackets;
    }
}