package com.oasys.engine.communication;

import com.oasys.engine.Main;
import com.oasys.engine.communication.packets.*;
import com.oasys.engine.logic.DataManager;

/**
 * Processes received Packets.
 */
public class PacketManager {

    private static PacketManager packetManagerInstance;

    private Communication communication;
    private DataManager dataManager;

    private PacketManager() {
        this.communication = Communication.getInstance();
        this.dataManager = DataManager.getInstance();
    }

    public static synchronized PacketManager getInstance() {
        if (PacketManager.packetManagerInstance == null) {
            PacketManager.packetManagerInstance = new PacketManager();
        }
        return PacketManager.packetManagerInstance;
    }

    /**
     * Passes on packet to other function.
     * @param packet sent packet.
     */
    public void handleIncomingPacket(Packet packet) {
        if (packet instanceof ClientAck) {
            this.handleClientAck((ClientAck) packet);
        } else if (packet instanceof GameEnd) {
            this.handleGameEnd((GameEnd) packet);
        } else if (packet instanceof GameState) {
            this.handleGameState((GameState) packet);
        /*} else if (packet instanceof JoinLobby) {
            this.handleJoinLobby((JoinLobby) packet);*/
        } else if (packet instanceof LobbyList) {
            this.handleLobbyList((LobbyList) packet);
        } else if (packet instanceof RecentGames) {
            this.handleRecentGames((RecentGames) packet);
        } else if (packet instanceof SuccessFeedback) {
            this.handleSuccessFeedback((SuccessFeedback) packet);
        }
    }

    /**
     * Handles packet Client Ack.
     * @param packet ClientAck.
     */
    public void handleClientAck(ClientAck packet) {
        int clientId = packet.getClientId();
        this.communication.setClientId(clientId);
        this.communication.setAcknowledged();

        // register as player (engine participants are always players)
        this.sendPlayerRegistration();

        if (Main.lobbyId != -1) {
            this.sendJoinLobby(Main.lobbyId);
        } else {
            System.out.println("waiting to be assigned to lobby");
        }
    }

    /**
     * Handles packet GameEnd.
     * @param packet GameEnd.
     */
    public void handleGameEnd(GameEnd packet) {
        this.sendLeaveLobby();
        System.out.println("game finished");
    }

    /**
     * Handles packet GameState.
     * @param gameState packet.
     */
    public void handleGameState(GameState gameState) {
        this.dataManager.setGameState(gameState);
    }

    /**
     * Handle packet JoinLobby.
     * @param joinLobby packet.
     */
    public void handleJoinLobby(JoinLobby joinLobby) {
        this.sendJoinLobby(joinLobby.getLobbyId());
    }

    /**
     * Handles packet LobbyList.
     * @param packet
     */
    public void handleLobbyList(LobbyList packet) {
        // nothing to do here since the engine participant never requests a LobbyList
    }

    /**
     *
     * @param packet
     */
    public void handleRecentGames(RecentGames packet) {
        // nothing to do here since the engine participant never requests RecentGames
    }

    public void handleSuccessFeedback(SuccessFeedback packet) {
        if (!packet.isSuccess()) {
            System.out.println("Got error response from the server: " + packet.getError());
        }
        System.out.println("Got success response from the server");
    }

    public void sendJoinLobby(int lobbyId) {
        if (this.dataManager.getCurrentLobby() != -1) {
            this.sendLeaveLobby();
        }
        JoinLobby packet = new JoinLobby(lobbyId, true); // engine is always a participant
        this.communication.sendPacket(packet);
        this.dataManager.setCurrentLobby(lobbyId);
        System.out.println("joining lobby" + lobbyId + " ..." );
    }

    public void sendLeaveLobby() {
        JoinLobby packet = new JoinLobby(-1, false); // -1 indicates leaving
        this.communication.sendPacket(packet);
        this.dataManager.setCurrentLobby(-1);
        System.out.println("leaving lobby ..." );
    }

    public void sendFinalBet(boolean isFirst, int id) {
        FinalBet packet = new FinalBet(isFirst, id);
        this.communication.sendPacket(packet);
    }

    public void sendMoveVisualized() {
        MoveVisualized packet = new MoveVisualized();
        this.communication.sendPacket(packet);
    }

    public void sendPlacePlayerCard(int spaceId, int movingDirection) {
        PlacePlayerCard packet = new PlacePlayerCard(spaceId, movingDirection);
        this.communication.sendPacket(packet);
    }

    public void sendPlayerRegistration() {
        PlayerRegistration packet = new PlayerRegistration(this.communication.getPlayerName());
        this.communication.sendPacket(packet);
    }

    public void sendRollDice() {
        RollDice packet = new RollDice();
        this.communication.sendPacket(packet);
    }

    public void sendStageBet(int clientId) {
        StageBet packet = new StageBet(clientId);
        this.communication.sendPacket(packet);
    }
}
