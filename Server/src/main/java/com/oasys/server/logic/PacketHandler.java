package com.oasys.server.logic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oasys.server.communication.packets.GameState;
import com.oasys.server.communication.packets.LobbyList;
import com.oasys.server.communication.packets.RecentGames;
import com.oasys.server.communication.packets.SuccessFeedback;
import com.oasys.server.data.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketHandler {
    private final BlockingQueue<PacketForQueue> queue = new LinkedBlockingQueue<>();
    Thread thread;
    ArrayList<JsonObject> playerList = new ArrayList<>();

    public ArrayList<JsonObject> getPlayerList() {
        return playerList;
    }

    public PacketHandler() {
        this.thread = new Thread(() -> {
            try {
                while (true)
                    handlePacket(queue.take());
                    //System.out.println("items in queue look like this:" +queue.take());

            } catch (InterruptedException e) {
                                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        this.thread.start();
    }

    public void offerPacket(PacketForQueue packetForQueue){
        queue.add(packetForQueue);
    }


    public void handlePacket(PacketForQueue packetList) throws IOException {
        /*The jsonObject and the clientHandler had to be packaged together in a class
        to be able to put into queue. After the PacketForQueue is taken out of Queue, it is reAssigned
        to the variables jsonObject and clientHandler
         */
        JsonObject jsonObject = packetList.getPacket();  //The Json sent from Client to us !!!
        ServerConnector.ClientHandler clientHandler = packetList.getClient();  //The client (id and connection etc.)

        System.out.println("jsonObject after Queue and ready for handling looks like this: " +jsonObject);
        Gson gson = new Gson();  //useful class that has methods to do stuff with Json (mainly toJson and fromJson)
        System.out.println("The packetHandler was called!!");
        //JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();

        String type = jsonObject.get("type").getAsString();
        System.out.println(type);

        if (type.equals("finalBet")) {
            //TODO
        }
        else if (type.equals("requestLobbyList")) {
            /* This as to test Sending a Lobby, before the real lobbies could be accessed.
            //Thread.sleep(10);
            ArrayList<Lobby> lobbies = new ArrayList<Lobby>();
            Lobby lobby0 = new Lobby(0, "Hello", createGameState());
            lobbies.add(lobby0);
            LobbyList lobbyList = new LobbyList(lobbies);
            String jsonString = lobbyList.convertToJson();
            JsonObject convertedLobbyList = gson.fromJson(jsonString, JsonObject.class);

             */

            // lobbies from LobbyManager
            ArrayList<Lobby> lobbiesU = LobbyManager.loadLobbies();
            LobbyList lobbyListU = new LobbyList(lobbiesU);
            String jsonStringU = lobbyListU.convertToJson();
            System.out.println(jsonStringU);
            JsonObject convertedLobbyListU = gson.fromJson(jsonStringU, JsonObject.class);

            System.out.println(convertedLobbyListU);
            gson.toJson(convertedLobbyListU, convertedLobbyListU.getClass(), clientHandler.getOut()); //put convertedLobbylist in the outputStream
            clientHandler.getOut().flush();   //actually sends theJson currently in client's out JsonWriter
        }
        else if (type.equals("rollDice")) {
            //TODO
        }

        else if (type.equals("requestRecentGames")) {
            //TODO
            //This send a makeshift recentGamesList, doesnt Access the real recentGames yet.
            RecentGame recentGame = new RecentGame(createGameState(), new ArrayList<Integer>());
            ArrayList<RecentGame> reee = new ArrayList<RecentGame>();
            reee.add(recentGame);
            RecentGames recentGames = new RecentGames(reee);
            String recentGamesString = recentGames.convertToJson();
            JsonObject convertedRecentGames = gson.fromJson(recentGamesString, JsonObject.class);
            System.out.println(convertedRecentGames);
            gson.toJson(convertedRecentGames, convertedRecentGames.getClass(), clientHandler.getOut()); //put convertedRecentGames in the outputStream
            clientHandler.getOut().flush();

        }

        else if (type.equals("playerRegistration")) {
            //TODO
            //PLAYER STILL HAS TO BE REGISTERED BEFORE !!
            JsonObject player = new JsonObject();
            player.addProperty("name", jsonObject.get("content").getAsJsonObject().get("playerName").getAsString());
            player.addProperty("playerId", clientHandler.getClientId());
            this.playerList.add(player);
            System.out.println(player);
            System.out.println(this.playerList);

            try (FileWriter writer = new FileWriter("playerListSave.json")) {
                gson.toJson(playerList, writer);
                System.out.println("playerList successfully written to file!");
            } catch (IOException e) {
                System.err.println("Error writing lobbies to file: " + e.getMessage());
            }
            System.out.println(LobbyManager.loadLobbies());



            String successFeedback = new SuccessFeedback(true, jsonObject, "").convertToJson();
            JsonObject convertedSuccessFeedback = gson.fromJson(successFeedback, JsonObject.class);
            gson.toJson(convertedSuccessFeedback, convertedSuccessFeedback.getClass(), clientHandler.getOut()); //put convertedRecentGames in the outputStream
            clientHandler.getOut().flush();

        }

        // Refactored joinLobby code using existing methods and classes

        // Refactored joinLobby code using existing methods and classes

        else if (type.equals("joinLobby")) {
            // Schritt 1: Paketdaten auslesen
            /*
            Object packet;
            JoinLobby joinLobbyPacket = gson.fromJson(jsonObject, JoinLobby.class); // Paket in JoinLobby umwandeln
            int lobbyId = joinLobbyPacket.getLobbyId(); // Lobby-ID extrahieren
            boolean joinAsPlayer = joinLobbyPacket.isJoinAsPlayer(); // Ob Spieler als Player beitreten möchte
             */

            int lobbyId = jsonObject.get("content").getAsJsonObject().get("lobbyId").getAsInt();
            boolean joinAsPlayer = jsonObject.get("content").getAsJsonObject().get("joinAsPlayer").getAsBoolean();
            System.out.println(lobbyId);
            System.out.println(joinAsPlayer);






            // Load open lobbies
            Lobby lobby = LobbyManager.findLobbyById(lobbyId); // Bestehende Methode, um eine spezifische Lobby zu finden

            // Schritt 3: Fehler prüfen (Lobby existiert nicht)
            if (lobby == null) {
                //ServerConnector.out(clientHandler, false,"Lobby mit ID " + lobbyId + " wurde nicht gefunden.");
                String successFeedback = new SuccessFeedback(false, jsonObject, "requested Lobby doesnt exist").convertToJson();
                JsonObject convertedSuccessFeedback = gson.fromJson(successFeedback, JsonObject.class);
                gson.toJson(convertedSuccessFeedback, convertedSuccessFeedback.getClass(), clientHandler.getOut()); //put convertedRecentGames in the outputStream
                clientHandler.getOut().flush();
                return;
            }

            //Code to test if Checking the Player Count works
           /* Player player1 = new Player(1, "player1",0, new ArrayList<BettingCard>(), "playing"  );
            Player player2 = new Player(2, "player2",0, new ArrayList<BettingCard>(), "playing"  );
            Player player3 = new Player(3, "player3",0, new ArrayList<BettingCard>(), "playing"  );
            //Player player4 = new Player(4, "player4",0, new ArrayList<BettingCard>(), "playing"  );
            lobby.getGameState().getPlayers().add(player1);
            lobby.getGameState().getPlayers().add(player2);
            lobby.getGameState().getPlayers().add(player3);
            //lobby.getGameState().getPlayers().add(player4);
            //System.out.println("Lobby after Adding players to test if player Max Count check works: "+lobby);
            */

            if (joinAsPlayer) {

                // Schritt 4: Spieleranzahl prüfen
                if (joinAsPlayer && lobby.getGameState().getPlayers().size() >= lobby.getGameState().getGameConfig().getPlayerCount()) {

                    String successFeedback = new SuccessFeedback(false, jsonObject, "Lobby ist voll. Maximale Spieleranzahl: " + String.valueOf(lobby.getGameState().getGameConfig().getPlayerCount())).convertToJson();
                    JsonObject convertedSuccessFeedback = gson.fromJson(successFeedback, JsonObject.class);
                    gson.toJson(convertedSuccessFeedback, convertedSuccessFeedback.getClass(), clientHandler.getOut()); //put convertedRecentGames in the outputStream
                    clientHandler.getOut().flush();
                    //ServerConnector.out(clientHandler, false, "Lobby ist voll. Maximale Spieleranzahl: " + lobby.getGameState().getGameConfig().getPlayerCount());
                    return;
                }

                // Schritt 5: Spieler erstellen und hinzufügen

                String name = "";
                for (int i = 0; i < this.playerList.size(); i++) {
                    if (clientHandler.getClientId() == this.playerList.get(i).get("playerId").getAsInt()) {
                        name = playerList.get(i).get("name").getAsString();
                    }

                }
                //Client that wants to join becomes a "Player"
                Player newPlayer = new Player(
                        clientHandler.getClientId(),
                        name,
                        0, // Inital amount of money
                        new ArrayList<>(), // Leere Liste für BettingCards
                        "playing" // Beispielwert für PlayerState, falls vorhanden
                );
                // Schritt 6: Spieler zur GameState-Spielerliste hinzufügen
                lobby.getGameState().getPlayers().add(newPlayer);
                LobbyManager.updateLobby(lobby);

                //SuccessFeedback sent after player joined the Lobby
                String successFeedback = new SuccessFeedback(true, jsonObject, "").convertToJson();
                JsonObject convertedSuccessFeedback = gson.fromJson(successFeedback, JsonObject.class);
                gson.toJson(convertedSuccessFeedback, convertedSuccessFeedback.getClass(), clientHandler.getOut()); //put convertedRecentGames in the outputStream
                clientHandler.getOut().flush();

                //GameState sent after joined as participant
                String gameStateAfterJoin = lobby.getGameState().convertToJson();
                JsonObject convertedGameStateAfterJoin = gson.fromJson(gameStateAfterJoin, JsonObject.class);
                gson.toJson(convertedGameStateAfterJoin, convertedGameStateAfterJoin.getClass(), clientHandler.getOut()); //put convertedconvertedGameStateAfterJoin in the outputStream
                clientHandler.getOut().flush(); //Send gameState from out
            }
            else {
                //Client gets added to lobby as Observer
                ArrayList<Integer> observerIds = lobby.getObserverIds();
                observerIds.add(clientHandler.getClientId());
                lobby.setObserverIds(observerIds);

                LobbyManager.updateLobby(lobby);
                System.out.println("Array of observerIds in JoinedLobby: " + LobbyManager.loadLobbies().get(lobbyId).getObserverIds());
                System.out.println("Your observerId: " + clientHandler.getClientId());
                //SuccessFeedback after joining as Observer
                String successFeedback = new SuccessFeedback(true, jsonObject, "").convertToJson();
                JsonObject convertedSuccessFeedback = gson.fromJson(successFeedback, JsonObject.class);
                gson.toJson(convertedSuccessFeedback, convertedSuccessFeedback.getClass(), clientHandler.getOut()); //put convertedRecentGames in the outputStream
                clientHandler.getOut().flush();

                //GameState sent after joined as Observer
                String gameStateAfterJoin = lobby.getGameState().convertToJson();
                JsonObject convertedGameStateAfterJoin = gson.fromJson(gameStateAfterJoin, JsonObject.class);
                gson.toJson(convertedGameStateAfterJoin, convertedGameStateAfterJoin.getClass(), clientHandler.getOut()); //put convertedconvertedGameStateAfterJoin in the outputStream
                clientHandler.getOut().flush(); //Send gameState from out

            }
            /*
            //SuccessFeedback after joining lobby went successful
            String successFeedback = new SuccessFeedback(true, jsonObject, "").convertToJson();
            JsonObject convertedSuccessFeedback = gson.fromJson(successFeedback, JsonObject.class);
            gson.toJson(convertedSuccessFeedback, convertedSuccessFeedback.getClass(), clientHandler.getOut()); //put convertedRecentGames in the outputStream
            clientHandler.getOut().flush();
            */



        }



        //else if (type.equals("joinLobby")) {
            /* joinlobby packet looks like this : {"type": "joinLobby","content": {"lobbyId": 0,"joinAsPlayer": true}}
             * 1.Prototype for Rule: 1.Search for LobbyId that client wants to join.
             * smth like: ConfigViewController.getLobbyList()
             * for i in lobbylist:
             *    if lobbyId = lobby.getId()
             *       if joinsAsPlayer == true:
             *          then check if num(playersList of Lobby to be joined) <= gameConfig.playerCount
             *              if Player in Lobbies smaller than MaxCount of players
             *                 then make Player class from the CLient that asked to join
             *                 (For Example clientHandler.getClientId()  und setzte Player Id=ClientID)
             *                 then Lobby.get("gameState").players.add(player mit ID: lobbyID)
             *
             *
             */
        //}
    }

    static GameState createGameState() {
        ArrayList<BoardSpace> boardSpaces = new ArrayList<BoardSpace>();

        ArrayList<Integer> camelIdsOn0 = new ArrayList<>();
        camelIdsOn0.add(1);
        boardSpaces.add(new BoardSpace(0, camelIdsOn0, null));

        ArrayList<Integer> camelIdsOn1 = new ArrayList<>();
        camelIdsOn1.add(4);
        boardSpaces.add(new BoardSpace(7, camelIdsOn1, null));

        ArrayList<Integer> camelIdsOn2 = new ArrayList<>();
        camelIdsOn2.add(2);
        boardSpaces.add(new BoardSpace(14, camelIdsOn2, null));

        ArrayList<Camel> camels = new ArrayList<>();
        camels.add(new Camel(1, "#dbc0bc"));
        camels.add(new Camel(4, "#e2a735"));
        camels.add(new Camel(2, "#18e70b"));
        camels.add(new Camel(3, "#e2a735"));
        camels.add(new Camel(100, "#18e70b"));
        camels.add(new Camel(200, "#20e75e"));
        GameConfig gameConfig = new GameConfig(4, 30, camels, 10, 5, 10000, 200, IllegalMovePenalty.FORFEIT_GAME, 1000000, 60);
        ArrayList<RolledDice> rolledDices = new ArrayList<RolledDice>();
        rolledDices.add(new RolledDice(1, 6));
        rolledDices.add(new RolledDice(4, 8));
        rolledDices.add(new RolledDice(2, 1));
        ArrayList<BettingCards> bettingCards = new ArrayList<>();
        bettingCards.add(new BettingCards(1, 6));
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<BettingCard> bettingCards1 = new ArrayList<>();
        bettingCards1.add(new BettingCard(1, 3));
        players.add(new Player(0, "Armin", 996, bettingCards1, "playing"));
        ArrayList<FinalBet> firstCamel = new ArrayList<>();
        firstCamel.add(new FinalBet(0, 1));
        ArrayList<FinalBet> lastCamel = new ArrayList<>();
        lastCamel.add(new FinalBet(0, 2));
        return new GameState(GamePhase.CREATED, boardSpaces, gameConfig, rolledDices, bettingCards, players, 43, 24, 10, new FinalBets(firstCamel, lastCamel));

    }
}