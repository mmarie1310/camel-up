package com.oasys.engine;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.oasys.engine.communication.packets.*;
import com.oasys.engine.data.*;
import com.oasys.engine.data.FinalBet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConverterTest {
    private final HashMap<String, Packet> packets = new HashMap<>();

    public ConverterTest() {
        // DATA OBJECTS
        ArrayList<BoardSpace> boardSpaces = new ArrayList<>();
        boardSpaces.add(new BoardSpace(0, new ArrayList<>(Arrays.asList(0, 1, 2)), null));
        boardSpaces.add(new BoardSpace(1, new ArrayList<>(Arrays.asList(-1, -2)), new PlayerCard(42, -1)));

        ArrayList<Camel> camels = new ArrayList<>();
        camels.add(new Camel(0, "#FF00FF")); 
        camels.add(new Camel(1, "#FF0000")); 
        camels.add(new Camel(2, "#0000FF")); 
        camels.add(new Camel(-1, "#00FF00")); 
        camels.add(new Camel(-2, "#FFFFFF")); 

        GameConfig gameConfig = new GameConfig(6, 3, camels, 6, 5, 60000, 5000,
                IllegalMovePenalty.FORFEIT_CURRENT_STAGE, 600000, 50);

        ArrayList<RolledDice> rolledDice = new ArrayList<>();
        rolledDice.add(new RolledDice(0, 3)); 
        rolledDice.add(new RolledDice(1, 6)); 

        ArrayList<BettingCards> bettingCards = new ArrayList<>(); //! list of BettingCards (not BettingCard!)
        bettingCards.add(new BettingCards(0, 3)); 
        bettingCards.add(new BettingCards(1, 0)); 
        bettingCards.add(new BettingCards(2, 2)); 

        ArrayList<Player> players = new ArrayList<>();
        ArrayList<BettingCard> p1BettingCards = new ArrayList<>(); //! list of BettingCard (not BettingCards!)
        p1BettingCards.add(new BettingCard(0, 2));
        p1BettingCards.add(new BettingCard(1, 3)); 
        p1BettingCards.add(new BettingCard(2, 2)); 
        ArrayList<BettingCard> p2BettingCards = new ArrayList<>(); //! list of BettingCard (not BettingCards!)
        ArrayList<BettingCard> p3BettingCards = new ArrayList<>(); //! list of BettingCard (not BettingCards!)
        p3BettingCards.add(new BettingCard(0, 2)); 
        p3BettingCards.add(new BettingCard(2, 5)); 
        players.add(new Player(123, "Hans", 0, p1BettingCards, PlayerState.PLAYING)); 
        players.add(new Player(321, "Peter", 100, p2BettingCards, PlayerState.CURRENT_STAGE_FORFEIT)); 
        players.add(new Player(111, "Marie", 0, p3BettingCards, PlayerState.GAME_FORFEIT)); 

        ArrayList<FinalBet> firstCamel = new ArrayList<>();
        firstCamel.add(new FinalBet(123, 1)); 
        firstCamel.add(new FinalBet(111, 0)); 
        firstCamel.add(new FinalBet(321, 2)); 
        ArrayList<FinalBet> lastCamel = new ArrayList<>();
        lastCamel.add(new FinalBet(111, 2)); 
        lastCamel.add(new FinalBet(321, 1)); 
        lastCamel.add(new FinalBet(123, 0)); 
        FinalBets finalBets = new FinalBets(firstCamel, lastCamel); 

        GameState gameState = new GameState(
                GamePhase.PLAYING,
                boardSpaces,
                gameConfig,
                rolledDice,
                bettingCards,
                players,
                42, 42, 42,
                finalBets); 
        Lobby lobby = new Lobby(0, "Lobby 0", gameState, new ArrayList<>(Arrays.asList(0, 1, 2, 3))); 

        // PACKETS
        // clientAck
        ClientAck clientAck = new ClientAck(1);
        this.packets.put("clientAck", clientAck); 

        // finalBet
        com.oasys.engine.communication.packets.FinalBet finalBet = new com.oasys.engine.communication.packets.FinalBet(false, 1);
        this.packets.put("finalBet", finalBet);

        // gameEnd
        GameEnd gameEnd = new GameEnd(lobby, new ArrayList<>(Arrays.asList(123, 111, 321)));
        this.packets.put("gameEnd", gameEnd);

        // gameState
        this.packets.put("gameState", gameState);

        // joinLobby
        JoinLobby joinLobby = new JoinLobby(-1, false);
        this.packets.put("joinLobby", joinLobby);

        // lobbyList
        LobbyList lobbyList = new LobbyList(new ArrayList<>(List.of(lobby)));
        this.packets.put("lobbyList", lobbyList);

        // moveVisualized
        MoveVisualized moveVisualized = new MoveVisualized();
        this.packets.put("moveVisualized", moveVisualized);

        // placePlayerCard
        PlacePlayerCard placePlayerCard = new PlacePlayerCard(2, -1); 
        this.packets.put("placePlayerCard", placePlayerCard);

        // playerRegistration
        PlayerRegistration playerRegistration = new PlayerRegistration("Hans");
        this.packets.put("playerRegistration", playerRegistration);

        // recentGames
        RecentGame recentGame = new RecentGame(gameState, new ArrayList<>(Arrays.asList(123, 111, 321))); 
        RecentGames recentGames = new RecentGames(new ArrayList<>(List.of(recentGame)));
        this.packets.put("recentGames", recentGames);

        // requestLobbyList
        RequestLobbyList requestLobbyList = new RequestLobbyList();
        this.packets.put("requestLobbyList", requestLobbyList);

        // requestRecentGames
        RequestRecentGames requestRecentGames = new RequestRecentGames(10);
        this.packets.put("requestRecentGames", requestRecentGames);

        // rollDice
        RollDice rollDice = new RollDice();
        this.packets.put("rollDice", rollDice);

        // stageBet
        StageBet stageBet = new StageBet(0);
        this.packets.put("stageBet", stageBet);

        // successFeedback
        JsonElement request = JsonParser.parseString("{\"type\": \"joinLobby\", \"content\": {\"lobbyId\": 1,\"joinAsPlayer\": true}}");
        SuccessFeedback successFeedback = new SuccessFeedback(false, request, "You are not a player!");
        this.packets.put("successFeedback", successFeedback);
    }

    @Test
    public void clientAckToJsonTest() {
        this.toJsonTest(ClientAck.class, "clientAck");
    }

    @Test
    public void clientAckFromJsonTest() {
        this.fromJsonTest(ClientAck.class, "clientAck");
    }

    @Test
    public void finalBetToJsonTest() {
        this.toJsonTest(com.oasys.engine.communication.packets.FinalBet.class, "finalBet");
    }

    @Test
    public void finalBetFromJsonTest() {
        this.fromJsonTest(com.oasys.engine.communication.packets.FinalBet.class, "finalBet");
    }

    @Test
    public void gameEndToJsonTest() {
        this.toJsonTest(GameEnd.class, "gameEnd");
    }

    @Test
    public void gameEndFromJsonTest() {
        this.fromJsonTest(GameEnd.class, "gameEnd");
    }

    @Test
    public void gameStateToJsonTest() {
        this.toJsonTest(GameState.class, "gameState");
    }

    @Test
    public void gameStateFromJsonTest() {
        this.fromJsonTest(GameState.class, "gameState");
    }

    @Test
    public void joinLobbyToJsonTest() {
        this.toJsonTest(JoinLobby.class, "joinLobby");
    }

    @Test
    public void joinLobbyFromJsonTest() {
        this.fromJsonTest(JoinLobby.class, "joinLobby");
    }

    @Test
    public void lobbyListToJsonTest() {
        this.toJsonTest(LobbyList.class, "lobbyList");
    }

    @Test
    public void lobbyListFromJsonTest() {
        this.fromJsonTest(LobbyList.class, "lobbyList");
    }

    @Test
    public void moveVisualizedToJsonTest() {
        this.toJsonTest(MoveVisualized.class, "moveVisualized");
    }

    @Test
    public void moveVisualizedFromJsonTest() {
        this.fromJsonTest(MoveVisualized.class, "moveVisualized");
    }

    @Test
    public void placePlayerCardToJsonTest() {
        this.toJsonTest(PlacePlayerCard.class, "placePlayerCard");
    }

    @Test
    public void placePlayerCardFromJsonTest() {
        this.fromJsonTest(PlacePlayerCard.class, "placePlayerCard");
    }

    @Test
    public void playerRegistrationToJsonTest() {
        this.toJsonTest(PlayerRegistration.class, "playerRegistration");
    }

    @Test
    public void playerRegistrationFromJsonTest() {
        this.fromJsonTest(PlayerRegistration.class, "playerRegistration");
    }

    @Test
    public void recentGamesToJsonTest() {
        this.toJsonTest(RecentGames.class, "recentGames");
    }

    @Test
    public void recentGamesFromJsonTest() {
        this.fromJsonTest(RecentGames.class, "recentGames");
    }

    @Test
    public void requestLobbyListToJsonTest() {
        this.toJsonTest(RequestLobbyList.class, "requestLobbyList");
    }

    @Test
    public void requestLobbyListFromJsonTest() {
        this.fromJsonTest(RequestLobbyList.class, "requestLobbyList");
    }

    @Test
    public void requestRecentGamesToJsonTest() {
        this.toJsonTest(RequestRecentGames.class, "requestRecentGames");
    }

    @Test
    public void requestRecentGamesFromJsonTest() {
        this.fromJsonTest(RequestRecentGames.class, "requestRecentGames");
    }

    @Test
    public void rollDiceToJsonTest() {
        this.toJsonTest(RollDice.class, "rollDice");
    }

    @Test
    public void rollDiceFromJsonTest() {
        this.fromJsonTest(RollDice.class, "rollDice");
    }

    @Test
    public void stageBetToJsonTest() {
        this.toJsonTest(StageBet.class, "stageBet");
    }

    @Test
    public void stageBetFromJsonTest() {
        this.fromJsonTest(StageBet.class, "stageBet");
    }

    @Test
    public void successFeedbackToJsonTest() {
        this.toJsonTest(SuccessFeedback.class, "successFeedback");
    }

    @Test
    public void successFeedbackFromJsonTest() {
        this.fromJsonTest(SuccessFeedback.class, "successFeedback");
    }

    private <PacketType extends Packet> void toJsonTest(Class<PacketType> packetTypeClass, String packetName) {
        PacketType packet = (PacketType) this.packets.get(packetName);
        String result = packet.convertTojson();
        String desired = ConverterTest.readPacketJson(packetName);
        Assertions.assertTrue(ConverterTest.compareJsons(result, desired));
        Assertions.assertFalse(result.contains("\n"));
    }

    private <PacketType extends Packet> void fromJsonTest(Class<PacketType> packetTypeClass, String packetName) {
        String json = ConverterTest.readPacketJson(packetName);
        Packet packet = Packet.convertFromJson(json);

        Assertions.assertTrue(packetTypeClass.isInstance(packet));

        PacketType result = (PacketType) packet;
        PacketType desired = (PacketType) this.packets.get(packetName);
        Assertions.assertEquals(result, desired); // requires PacketType and all attributes (and their attributes) to have equals implemented
    }

    private static String readPacketJson(String packetName) {
        String fileName = "jsonPackets/" + packetName + ".json";

        ClassLoader classLoader = ConverterTest.class.getClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        return reader.lines().collect(Collectors.joining());
    }

    private static boolean compareJsons(String json1, String json2) {
        JsonElement o1 = JsonParser.parseString(json1);
        JsonElement o2 = JsonParser.parseString(json2);
        return o1.equals(o2);
    }

}
