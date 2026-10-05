package com.oasys.server.logic;

//import com.google.gson.JsonObject;
//import com.google.gson.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.oasys.server.communication.packets.ClientAck;
import com.oasys.server.communication.packets.GameState;
import com.oasys.server.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerConnector {

    public static HashMap<Integer, ClientHandler> clients = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(ServerConnector.class);
    public static PacketHandler packetHandler = new PacketHandler();

    private ServerSocket serverSocket;
    /* ClientId is starting from zero, incrementing by 1. Since there are no Registrations,
       if Server restarts, all ClientIDs reset and start from zero. Meaning you can`t reconnect to a game after
       disconnecting */
    public static int clientIdCounter = 0;

    public static void out(ClientHandler clientHandler, boolean success, String message) {
        try {
            JsonObject response = new JsonObject();
            response.addProperty("success", success);
            response.addProperty("message", message);

            clientHandler.getOut().beginObject();
            new Gson().toJson(response, response.getClass(), clientHandler.getOut());
            clientHandler.getOut().endObject();
            clientHandler.getOut().flush();
        } catch (IOException e) {
            LOG.error("Fehler beim Senden der Nachricht: " + e.getMessage());
        }
    }


    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                new ClientHandler(serverSocket.accept(), clientIdCounter).start();
            clientIdCounter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }

    }

    public void stop() {
        try {

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* sendMessage muss richtigen Client oder evlt nur den richtigen PrintWriter "out" von Client
       aus der Hashmap clients bekommen, dann diesem die message schicken
       */
    // public void sendMessage(PrintWriter richtigesOut, message) { richtigesOut.println(message)}


    public static class ClientHandler extends Thread {
        /*//Client and Server both have a socket, through which they are connected.
            The client's Socket is saved here.
         */
        private final Socket clientSocket;
        /*Gson is a Class with useful methods to convert Jsons from String to Json and vice versa,
          or to send the Jsons via the Jsonwriter or receive them through the JsonReader.
          Examples: gson.fromJson(), gson.toJson(),

         */
        private final Gson gson = new Gson();
        public JsonWriter out;    //out is the Stream, through which the Server sends to the client
        private JsonReader in;     //in is the Stream, through which the Client sends to the Server
        private final int clientId;

        public ClientHandler(Socket socket, int clientIdCounter) {
            this.clientSocket = socket;
            this.clientId = clientIdCounter;
        }

        //Return the JsonWriter for PacketHandler to use
        public JsonWriter getOut() {return out;}
        //Return the JsonWriter for PacketHandler to use
        public int getClientId() {return clientId;}

        public void run() {

            //Create ClientAck and send to Client
            ClientAck clientAck0 = new ClientAck(this.clientId);
            String json0 = clientAck0.convertToJson();
            JsonObject convertedClientAck = gson.fromJson(json0, JsonObject.class);

            try {
                out = new JsonWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                in = new JsonReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("New client with id " + this.clientId + " connected");


                gson.toJson(convertedClientAck, convertedClientAck.getClass(), this.getOut()); //put convertedClientAck into outputstream
                try {
                    this.getOut().flush();  //Send ClientAck
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                while (clientSocket.isConnected()) {
                    JsonObject jsonObject = gson.fromJson(in, JsonObject.class);
                    System.out.println(jsonObject + " received");

                    PacketForQueue packetForQueue = new PacketForQueue(jsonObject, this);
                    packetHandler.offerPacket(packetForQueue);

                }

                in.close();
                out.close();
                clientSocket.close();
                System.out.println("Socket closed");

            } catch (IOException e) {
                LOG.debug(e.getMessage());
            } //catch (InterruptedException e) {
               // throw new RuntimeException(e);
            //}
        }

        GameState createGameState() {
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

    public static void main(String[] args) {
        ServerConnector server = new ServerConnector();
        server.start(62263);
    }

}
