package com.oasys.server.logic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerListManager {
    private static final Gson gson = new Gson();

    public static void clearPlayerListSave() {
        try (FileWriter writer = new FileWriter("playerListSave.json")) {
            // Write an empty array to clear the file
            writer.write("[]");
        } catch (IOException e) {
            System.err.println("Error clearing playerList file: " + e.getMessage());
        }
    }

    public static ArrayList<JsonObject> loadPlayerList() {
        ArrayList<JsonObject> playerList = new ArrayList<>();

        try (FileReader reader = new FileReader("playerListSave.json")) {
            // Parse the JSON array from the file
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            // Loop through the JsonArray and convert each element into a Lobby object
            for (JsonElement element : jsonArray) {
                JsonObject player = gson.fromJson(element, JsonObject.class);
                playerList.add(player);
            }
        } catch (IOException e) {
            System.err.println("Error reading lobbies file: " + e.getMessage());
        }

        return playerList;
    }

   /* public static void clearLobbiesFile() {
        try (FileWriter writer = new FileWriter("lobbies.json")) {
            // Write an empty array to clear the file
            writer.write("[]");
        } catch (IOException e) {
            System.err.println("Error clearing lobbies file: " + e.getMessage());
        }
    }*/

    public static JsonObject findPlayerByName(String playerName) {
        return loadPlayerList().stream()
                .filter(player -> player.get("name").getAsString().equals(playerName))
                .findFirst()
                .orElse(null);
    }

    public static void updatePlayerList(JsonObject player) {
        ArrayList<JsonObject> playerList = loadPlayerList();

        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).get("name") == player.get("name")) {
                playerList.set(i, player);

                try (FileWriter writer = new FileWriter("playerListSave.json")) {
                    gson.toJson(playerList, writer);
                    System.out.println("playerList successfully written to file!");
                } catch (IOException e) {
                    System.err.println("Error writing lobbies to file: " + e.getMessage());
                }
                System.out.println("Lobbies after updating: " + PlayerListManager.loadPlayerList());

                break;
            }
        }


        //System.out.println("Lobby " + lobby.getLobbyId() + " wurde aktualisiert.");
    }
}
